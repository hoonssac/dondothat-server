package org.bbagisix.finproduct.service;

import org.bbagisix.exception.BusinessException;
import org.bbagisix.exception.ErrorCode;
import org.bbagisix.finproduct.dto.RecommendedSavingDTO;
import org.bbagisix.finproduct.mapper.FinProductMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.List;

// 적금상품 추천 서비스
// 하이브리드 방식: 백엔드 1차 필터링 + LLM 2차 지능형 추천
@Log4j2
@Service
@RequiredArgsConstructor
public class RecommendationService {
    
    private final FinProductMapper finProductMapper;

    // 사용자 맞춤 적금상품 1차 필터링
    // 명백한 불가능 케이스(나이 제한, 직업 제한)를 제외하고 필터링된 데이터를 반환
    public List<RecommendedSavingDTO> getFilteredSavings(Long userId, Integer limit) {
        try {
            log.info("사용자 {}에 대한 1차 필터링 시작 - limit: {}", userId, limit);
            
            if (userId == null) {
                throw new BusinessException(ErrorCode.USER_ID_REQUIRED, "사용자 ID가 필요합니다");
            }
            
            // DB에서 나이 및 직업 필터링 후 조회
            List<RecommendedSavingDTO> filteredProducts = finProductMapper.findRecommendedSavings(userId, limit);
            
            if (filteredProducts == null || filteredProducts.isEmpty()) {
                log.warn("사용자 {}에 대한 추천 가능한 상품이 없습니다", userId);
                throw new BusinessException(ErrorCode.FINPRODUCT_NOT_FOUND, "추천 가능한 금융상품이 없습니다");
            }
            
            log.info("사용자 {}에 대한 DB 필터링 완료 - {}개 상품", userId, filteredProducts.size());
            
            return filteredProducts;
            
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("1차 필터링 중 예상치 못한 오류 발생 - userId: {}, limit: {}, 오류: {}", 
                    userId, limit, e.getMessage(), e);
            throw new BusinessException(ErrorCode.FINPRODUCT_NOT_FOUND, e);
        }
    }
}