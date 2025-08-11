package org.bbagisix.finproduct.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.bbagisix.finproduct.domain.SavingBaseVO;
import org.bbagisix.finproduct.domain.SavingOptionVO;
import org.bbagisix.finproduct.dto.RecommendedSavingDTO;

import java.util.List;

@Mapper
public interface FinProductMapper {

    void insertOrUpdateBase(SavingBaseVO baseVO);

    void insertOrUpdateOption(SavingOptionVO optionVO);

    // 복합 키(fin_co_no, fin_prdt_cd, dcls_month)를 기준으로 saving_base 테이블의 PK를 조회
    // 옵션 정보를 saving_option 테이블에 저장할 때, 방금 알아낸 `saving_base_id` 값을 `saving_base_id` 컬럼에 넣음
    Long findSavingBaseId(SavingBaseVO baseVO);
    

    // 추천 적금 상품 조회 (백엔드 필터링)
    // 사용자 정보(나이, 직업, 주거래은행)를 기반으로 불가능한 상품 제외
    // LLM에 전달할 필터링된 적금 상품 목록
    List<RecommendedSavingDTO> findRecommendedSavings(
            @Param("userId") Long userId,
            @Param("limit") Integer limit
    );
}
