from fastapi import FastAPI
from pydantic import BaseModel
from openai import OpenAI
import asyncio
from dotenv import load_dotenv
from typing import List
from datetime import datetime
import os
import re

load_dotenv()
client = OpenAI(api_key=os.getenv("API_KEY"))
app = FastAPI()

class Exp(BaseModel):
    expenditure_id: int
    description: str

class ExpsList(BaseModel):
    exps: List[Exp]

class ExpForAnalytics(BaseModel):
    category_id: int
    amount: int
    expenditure_date: datetime

class ExpsAnalytics(BaseModel):
    exps: List[ExpForAnalytics]

category_keyword_map = {
    1: ["우아한형제들", "요기요", "배달의민족", "배민", "쿠팡이츠"],
    2: ["스타벅스", "투썸", "컴포즈", "매머드", "커피", "카페", "베이커리", "메가커피", "이디야", "폴바셋"],
    3: ["신세계백화점", "현대백화점", "롯데백화점", "올리브영", "무신사", "쿠팡", "11번가", "G마켓", "SSG.COM", "롯데온", "네이버스마트스토어"],
    4: ["카카오T", "택시", "우버", "마카롱", "T 블루"],
    5: ["CU", "씨유", "GS", "지에스", "세븐일레븐", "이마트24", "편의점", "미니스톱"],
    6: ["CGV", "메가박스", "롯데시네마", "예스24", "인터파크", "도서", "공연", "문화", "티켓", "교보문고"],
    7: ["맥주", "소주", "술집", "포차", "홈술", "와인", "역전할머니", "더부스"],
    8: ["버스", "지하철", "교통카드", "티머니", "T머니", "코레일", "KTX", "공항철도", "카카오모빌리티"],
    9: ["약국", "병원", "의원", "한의원", "치과", "안과", "피부과"],
    10: ["이마트", "홈플러스", "마트", "다이소", "코스트코", "마켓컬리", "쿠팡 로켓프레시"],
    11: ["김밥", "분식", "식당", "라멘", "파스타", "카츠", "맥도날드", "우동", "써브웨이", "칼국수", "버거", "스시", 
         "롯데리아", "버거킹", "신전떡볶이", "백향목분식", "본도시락", "BHC", "교촌치킨", "미분당", "홍콩반점"]
}

def keyword_filtering(desc: str) -> int:
    for id, keywords in category_keyword_map.items():
        for k in keywords:
            if k in desc:
                return id 
    return -1

def extract_number_from_response(response_text: str) -> int:
    """응답에서 숫자를 추출하고 유효성 검사"""
    # 숫자만 추출
    numbers = re.findall(r'\d+', response_text)
    if numbers:
        category_id = int(numbers[0])
        # 1~7 범위만 허용
        if 1 <= category_id <= 7:
            return category_id
    # 기본값으로 7 (술/유흥) 반환
    return 7

def classify_category(desc):
    filtered = keyword_filtering(desc)
    if filtered != -1:
        return filtered

    messages = [
        {
            "role": "system",
            "content": """당신은 한국의 소비 패턴을 정확히 이해하는 금융 분석 전문가입니다. 
소비 내역을 정확한 카테고리로 분류하는 것이 당신의 임무입니다.

분류 규칙:
1. 반드시 1~7 중 하나의 숫자만 출력하세요
2. 설명이나 다른 텍스트는 절대 포함하지 마세요
3. 애매한 경우 가장 가능성이 높은 카테고리를 선택하세요
4. 8~14번 카테고리는 존재하지 않습니다"""
        },
        {
            "role": "user", 
            "content": f"""소비내역: "{desc}"

다음 카테고리 중 하나로 분류하세요:

1: 배달음식 (배달의민족, 요기요, 쿠팡이츠 등)
2: 카페/간식 (스타벅스, 투썸, 커피전문점, 베이커리 등)
3: 쇼핑 (백화점, 온라인쇼핑, 패션, 화장품 등)
4: 택시 (카카오T, 우버, 일반택시 등)
5: 편의점 (CU, GS25, 세븐일레븐, 이마트24 등)
6: 문화 (영화관, 공연, 도서, 스포츠, 티켓 등)
7: 술/유흥 (술집, 맥주, 소주, 와인 등)

숫자만 출력하세요:"""
        }
    ]
    
    response = client.chat.completions.create(
        model="gpt-4o",
        messages=messages,
        temperature=0,
        max_tokens=10
    )
    
    response_text = response.choices[0].message.content.strip()
    return extract_number_from_response(response_text)

async def classify_category_async(desc):
    loop = asyncio.get_event_loop()
    return await loop.run_in_executor(None, lambda: classify_category(desc))

# 분류 API
@app.post("/classify")
async def classify(list: ExpsList):
    tasks = [classify_category_async(e.description) for e in list.exps]
    categories = await asyncio.gather(*tasks)
    results = [
        {"expenditure_id": e.expenditure_id, "category_id": int(c)}
        for e, c in zip(list.exps, categories)
    ]
    return {"results": results}

# 분석 API
@app.post("/analysis")
async def analysis(list: ExpsAnalytics):
    simplified = [
        {"category_id": e.category_id, 
        "amount": e.amount, 
        "expenditure_date": e.expenditure_date.strftime("%Y-%m-%d")
        } for e in list.exps]
    
    messages = [
        {
            "role": "system",
            "content": """당신은 개인 금융 관리 전문가입니다. 사용자의 소비 패턴을 분석하여 과소비가 심한 카테고리를 찾아내는 것이 당신의 역할입니다.

분석 기준:
1. 최근 30일 vs 이전 30일 지출 증가율
2. 사치성 소비 카테고리 가중치 (배달음식, 카페, 쇼핑, 택시, 편의점, 문화, 술/유흥)
3. 절대 지출 금액의 크기
4. 새롭게 등장한 소비 카테고리

출력 형식: 카테고리 번호 3개를 쉼표로 구분 (예: 1,3,7)
1~7 범위의 숫자만 사용하세요."""
        },
        {
            "role": "user",
            "content": f"""다음 소비 데이터를 분석하여 과소비가 심한 카테고리 상위 3개를 선정하세요:

소비내역: {simplified}

카테고리 설명:
1: 배달음식, 2: 카페/간식, 3: 쇼핑, 4: 택시, 5: 편의점
6: 문화, 7: 술/유흥

분석 조건:
- 지난 60~30일 대비 최근 30일 지출이 급증한 카테고리
- 배달음식(1), 카페(2), 쇼핑(3), 택시(4), 편의점(5), 문화(6), 술/유흥(7) 사치성 소비 우선
- 절대 금액이 큰 카테고리 우선
- 새로운 소비 패턴도 고려

상위 3개 카테고리 번호만 쉼표로 구분하여 출력:"""
        }
    ]
    
    response = client.chat.completions.create(
        model="gpt-4o", 
        messages=messages,
        temperature=0,
        max_tokens=20
    )
    
    res = response.choices[0].message.content.strip()
    
    # 응답에서 숫자 추출 및 유효성 검사
    numbers = re.findall(r'\b([1-7])\b', res)  # 1~7만 매치
    result_list = [int(x) for x in numbers[:3]]  # 최대 3개만
    
    # 결과가 없으면 기본값 반환
    if not result_list:
        result_list = [1, 3, 7]  # 기본 과소비 카테고리
        
    return {"results": result_list}
