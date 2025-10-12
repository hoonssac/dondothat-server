<img width="1022" height="503" alt="image" src="https://github.com/user-attachments/assets/5fe9ea4d-30d5-4a0d-8968-8cbe24bb797d" />


> [KB] IT's Your Life 6기<br>
> 팀명 : 빽이식스<br>
> 개발 기간 : 2025.06 ~ 2025.08


# 📄 프로젝트 소개
**돈두댓**은 Z세대의 지속적인 절약 습관 형성을 돕고, 실제 저축과 자산 형성으로까지 연결하는 금융 챌린지 플랫폼입니다.<br>
고물가·고금리·고환율 시대가 도래하면서, Z세대 사이에서는 "정말 필요할 때만 산다"는 **YONO(You Only Need Once)** 트렌드가 확산되고 있습니다.<br>
하지만 단순한 의지만으로는 장기적인 절약이 어렵다는 문제를 인식하고,<br>체계적인 챌린지 시스템과 AI 분석, 실시간 커뮤니티를 통해 지속 가능한 소비 습관 개선을 지원합니다.

### 핵심 가치
- **AI 기반 개인화**: 사용자 소비 패턴 분석을 통한 맞춤형 챌린지 추천
- **체계적인 강제성**: 기한과 규칙이 명확한 챌린지 구조
- **즉각적인 보상**: 아낀 금액 가시화, 티어 시스템, 저축 연동
- **커뮤니티 동기부여**: 실시간 채팅을 통한 정보 공유와 공감대 형성


# 🙋🏻 팀원
<table>
  <tr>
    <td align="center">
      <a href="https://github.com/hoonssac">
        <img width="80" height="150" src="https://github.com/hoonssac.png" alt="서상훈"/>
        <br/><strong>서상훈</strong><br/>
        <a>(LEADER)</a>
      </a>
    </td>
    <td align="left">
      • CI/CD 파이프라인 구축<br/>
      • JWT 기반 인증/인가 기능 개발<br/>
      • <a href="https://velog.io/@b1uesoda/%EA%B8%88%EC%9C%B5%EC%83%81%ED%92%88-%EC%B6%94%EC%B2%9C-API-2%EC%B4%88%EC%97%90%EC%84%9C-50ms%EB%A1%9C-%EA%B0%9C%EC%84%A0%ED%95%98%EA%B8%B0-Redis-%EC%BA%90%EC%8B%B1-%EC%A0%81%EC%9A%A9%EA%B8%B0">적금 상품 추천 성능 개선<br/>
    </td>
  </tr>
  <tr>
    <td align="center">
      <a href="https://github.com/Moon0116">
        <img width="80" height="150" src="https://github.com/Moon0116.png" alt="문상혁"/>
        <br/><strong>문상혁</strong><br/>
        <a>(MEMBER)</a>
      </a>
    </td>
    <td align="left">
      • 소비 내역 CRUD 기능 개발<br/>
      • LLM 기반 적금 상품 추천 기능 개발<br/>
      • 프론트엔드 성능 개선<br/>
    </td>
  </tr>
  <tr>
    <td align="center">
      <a href="https://github.com/Hyunji-JANG">
        <img width="80" height="150" src="https://github.com/Hyunji-JANG.png" alt="장현지"/>
        <br/><strong>장현지</strong><br/>
        <a>(MEMBER)</a>
      </a>
    </td>
    <td align="left">
      • WebSocket기반 실시간 채팅 기능 개발<br/>
      • 마이페이지 기능 개발<br/>
      • 사용자 티어 시스템 개발<br/>
    </td>
  </tr>
  <tr>
    <td align="center">
      <a href="https://github.com/hyungEee">
        <img width="80" height="150" src="https://github.com/hyungEee.png" alt="최지형"/>
        <br/><strong>최지형</strong><br/>
        <a>(MEMBER)</a>
      </a>
    </td>
    <td align="left">
      • LLM 기반 카테고리 분류 로직 개발<br/>
      • 분류 로직 성능 개선<br/>
      • 저금통 기능 개발<br/>
    </td>
  </tr>
  <tr>
    <td align="center">
      <a href="https://github.com/westsunh">
        <img width="80" height="150" src="https://github.com/westsunh.png" alt="김서현"/>
        <br/><strong>김서현</strong><br/>
        <a>(MEMBER)</a>
      </a>
    </td>
    <td align="left">
      • CODEF API를 활용한 계좌 연동 기능 개발<br/>
      • AES-256 암호화로 계좌 정보 안전 관리<br/>
      • 프론트엔드 기초 환경 구축<br/>
    </td>
  </tr>
  <tr>
    <td align="center">
      <a href="https://github.com/minji677">
        <img width="80" height="150" src="https://github.com/minji677.png" alt="김민지"/>
        <br/><strong>김민지</strong><br/>
        <a>(MEMBER)</a>
      </a>
    </td>
    <td align="left">
      • 챌린지 시스템 개발<br>
      • UI/UX 디자인<br/>
      • 사용자 대시보드 개발<br/>
    </td>
  </tr>
</table>




# 🎯 주요 기능
## 🏦 입출금 계좌 연동 및 소비 카테고리 자동 분류
Codef API를 활용하여 사용자의 입출금 계좌를 연동하고, 입출금 계좌 거래 내역의 상호명을 분석하여 자동으로 카테고리를 분류합니다.

<img width="150" height="837" alt="1 4 2 계좌 연결 - 은행 선택" src="https://github.com/user-attachments/assets/a3f96eeb-a5ef-442a-b4bd-4a01006b2d23" />
<img width="150" height="837" alt="1 4 3 계좌 연결 - 정보 입력" src="https://github.com/user-attachments/assets/732f97e2-ebea-4c99-b556-1f6b4f2e07f1" />
<img width="150" height="837" alt="1 4 4 계좌 연결 - 실제 계좌 연결" src="https://github.com/user-attachments/assets/45dab293-8688-401d-b08f-f9f306f3f2c3" />
<img width="150" height="837" alt="1 4 5 계좌 연결 - 연결 완료" src="https://github.com/user-attachments/assets/f67c84d6-29b3-4f87-b026-dfcb01321bf1" />
<img width="150" height="839" alt="2 1 1 메인 페이지- 참여중인 챌린지 없음" src="https://github.com/user-attachments/assets/106afab6-b33f-4100-a52e-908c71cdb4e2" />
<img width="150" height="841" alt="3 1 소비내역 페이지" src="https://github.com/user-attachments/assets/d9460572-bf43-4bde-ad9e-5821ebfb5247" />


### 특징
- Codef API를 통한 계좌 연동
- API 제한을 고려해, 10분마다 자동으로 소비 내역 동기화
- 상호명에 자주 출현하는 키워드 기반으로 1차 분류
- 1차에서 분류되지 않은 항목을 LLM으로 분류


### 보안
- AES-256 암호화로 계좌 정보 안전 관리
- MyBatis TypeHandler 적용으로 DB 입출력 시 자동 암복호화
- 서비스 로직에서 민감 데이터 평문 노출 방지






## 🤖 AI 맞춤형 챌린지 기능
사용자의 최근 소비 패턴을 AI가 분석하여 과소비 카테고리를 찾아내고, 맞춤형 챌린지 3개를 추천합니다.

<img width="150" height="839" alt="5 3 2 챌린지 선택 - 선택 완료" src="https://github.com/user-attachments/assets/bcc18f92-5ae8-4876-9a64-d4166235624f" />
<img width="150" height="839" alt="5 3 3 챌린지 선택 - 날짜 입력" src="https://github.com/user-attachments/assets/45dfdf64-6feb-4c3f-ae5c-bef021291deb" />
<img width="150" height="839" alt="5 4 챌린지 - 도전 중인 챌린지 메인" src="https://github.com/user-attachments/assets/352e38ef-2387-4441-b87b-c845ca9389ac" />
<img width="150" height="839" alt="5 6 저금 완료" src="https://github.com/user-attachments/assets/b533e300-f236-4f88-9cd0-ce90af75c5c0" />
<img width="150" height="839" alt="5 7 티어 업" src="https://github.com/user-attachments/assets/deba66d1-45c9-4c48-9394-54aa27a2abf3" />
<img width="150" height="839" alt="6 5 적금 추천" src="https://github.com/user-attachments/assets/f8a9ff40-f4a7-4c93-b773-ef30ae1abd10" />




### 챌린지 기능
- 최근 30일 vs 지난 30일(60일 전~30일 전) 소비 내역 비교
- 카테고리별 지출 증가량 분석
- AI가 3개의 추천 챌린지 제시
- 챌린지 기간 설정 가능
- 매일 자정, 시스템이 자동으로 소비 내역 감지
- 챌린지 카테고리 소비 여부에 따라 성공/실패 판정

### 챌린지 보상
- 아낀 금액을 실제 저금통 계좌로 이체
- 챌린지 성공에 따른 티어 상승
- 사용자 프로필(직업/나이) 기반 적합한 적금 상품 제안
- 금융감독원 API를 활용한 최신 적금 상품 데이터 자동 동기화


## 💬 실시간 채팅
WebSocket과 Redis Pub/Sub을 활용하여 동일 챌린지 참여자끼리 실시간으로 소통할 수 있는 채팅방을 제공합니다.

<img width="150" height="839" alt="6 2 채팅 - 챌린지 참여" src="https://github.com/user-attachments/assets/22ff81cf-efc1-4495-9854-25339ae94416" />



### 특징
- 동일 챌린지 참여자 간 실시간 소통
- WebSocket + STOMP 프로토콜
- Redis Pub/Sub을 통한 메시지 브로드캐스팅
- 챌린지 종료 시 자동 퇴장(장기 사용자 폐쇄성 방지)






# ⚒️ 기술 스택

### Backend
- **Language**: Java 17
- **Framework**: Spring Framework 5.3.37, Spring MVC, Spring Security 5.8.12, Spring WebSocket
- **Authentication**: JWT (jjwt 0.11.5), OAuth2 (Spring Security OAuth2 Client)
- **Database**: MySQL 8.1.0, MyBatis 3.4.6, HikariCP
- **Cache**: Redis (spring-data-redis 2.7.18), Lettuce Core 6.2.7
- **Build Tool**: Gradle
- **Logging**: Log4j2 2.18.0, SLF4J
- **Security**: AES-256 암호화
- **Validation**: Hibernate Validator 6.0.13
- **Utils**: Lombok, Jackson

### Frontend
- **Framework**: Vue.js 3
- **Build Tool**: Vite
- **Styling**: Tailwind CSS (커스터마이징 + 반응형 디자인)
- **State Management**: Pinia
- **Deployment**: Netlify

### AI & Data Processing
- **AI API**: OpenAI GPT-4
- **AI Server**: FastAPI (별도 서버로 분리)

### Infrastructure
- **Cloud**: AWS EC2
- **Containerization**: Docker (Spring, MySQL, Redis, FastAPI 4개 컨테이너)
- **Reverse Proxy**: Nginx
- **SSL/TLS**: Let's Encrypt
- **CI/CD**: GitHub Actions

### External APIs
- **금융 데이터**: Codef API
- **금융상품 정보**: 금융감독원 API
- **AI**: OpenAI API


## 시스템 아키텍처
<img width="1404" height="697" alt="image" src="https://github.com/user-attachments/assets/8b8f3ccf-6cf8-4fa7-835b-347ba10fd20a" />

### 아키텍처 특징

**1. 컨테이너 기반 설계**
- Docker를 활용한 모듈별 독립 관리
- 장애 발생 시 신속한 복구
- 각 서비스의 독립적인 스케일링

**2. AI 서버 분리**
- FastAPI를 별도 서버로 운영
- GPT 호출 등 처리 시간이 긴 작업 전담
- 핵심 서비스 성능 저하 방지
- 트래픽 증가에 유연한 확장

**3. Redis 활용**
- 실시간 메시지 브로커 (Pub/Sub)
- 메모리 기반 고속 처리 (ms 단위)
- 금융상품 데이터 캐싱

**4. 보안 통신**
- Nginx + Let's Encrypt HTTPS 적용
- JWT 토큰 기반 인증
- AES-256 민감 정보 암호화



## 데이터베이스 설계
<img width="1709" height="669" alt="image" src="https://github.com/user-attachments/assets/71907fe4-6154-4ccd-b841-0d8f61f0abb1" />

### 주요 테이블

**1. User (사용자)**
- 사용자 기본 정보, OAuth2 정보, 계좌 연동 상태

**2. Challenge (챌린지)**
- 챌린지 정보, 카테고리, 기간
- `status`: ongoing, completed, failed, closed
- 챌린지 종료 시 `closed`로 변경하여 참여 내역 유지

**3. User_Challenge (사용자-챌린지 매핑)**
- 사용자별 챌린지 참여 내역
- 시작일, 종료일, 진척도, 아낀 금액

**4. Expense (소비 내역)**
- 지출 내역, 카테고리, 금액, 날짜
- `deleted_at`: Soft Delete 방식 (NULL → 삭제일 기록)
- `user_modified`: 사용자 수정 여부 (true 시 중복 생성 방지)

**5. Asset (자산)**
- 계좌 정보 (AES-256 암호화)
- 메인/서브 계좌 구분

**6. Saving (저축)**
- 저축 내역, 저금 금액

**7. Chat_Message (채팅 메시지)**
- 메시지 내용, 발신자, 챌린지 ID, 타임스탬프

**8. Tier (티어)**
- 티어 정보, 혜택

### ERD 주요 설계 포인트

**1. Soft Delete 패턴**
- `deleted_at` 필드로 데이터 물리 삭제 방지
- Codef API 호출 시 중복 생성 방지
- 데이터 내역 관리 강화

**2. 사용자 수정 이력 관리**
- `user_modified` 플래그로 사용자 직접 수정 구분
- API 재호출 시 수정된 데이터 보존

**3. 챌린지 상태 관리**
- 4가지 상태값으로 생명주기 관리
- `closed` 상태로 이력 보존



## 프로젝트 구조

```
dondothat-server/
├── src/
│   ├── main/
│   │   ├── java/org/bbagisix/
│   │   │   ├── analytics/         # 분석 기능
│   │   │   ├── asset/              # 자산 관리 (계좌 연동, AES 암호화)
│   │   │   ├── category/           # 카테고리 관리
│   │   │   ├── challenge/          # 챌린지 시스템 (추천, 참여, 판정)
│   │   │   ├── chat/               # 실시간 채팅 (WebSocket, Redis Pub/Sub)
│   │   │   ├── classify/           # AI 카테고리 분류
│   │   │   ├── common/             # 공통 컴포넌트 (예외 처리, 유틸리티)
│   │   │   ├── expense/            # 지출 관리 (CRUD, 통계)
│   │   │   ├── finproduct/         # 금융상품 추천 (금감원 API, 캐시)
│   │   │   ├── mypage/             # 마이페이지
│   │   │   ├── saving/             # 저축 관리 (저금통, 이체)
│   │   │   ├── tier/               # 티어 시스템
│   │   │   └── user/               # 사용자 관리 (인증, OAuth2)
│   │   ├── resources/
│   │   │   ├── mappers/            # MyBatis 매퍼 XML
│   │   │   ├── mybatis-config.xml  # MyBatis 설정
│   │   │   ├── log4j2.xml          # 로깅 설정
│   │   │   └── application.properties
│   │   └── webapp/
│   │       └── WEB-INF/
│   └── test/
│       └── java/org/bbagisix/      # 테스트 코드
├── build.gradle                     # Gradle 빌드 설정
├── Dockerfile                       # Docker 컨테이너 설정
└── README.md
```
