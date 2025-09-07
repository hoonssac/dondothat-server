package org.bbagisix.finproduct.service;

import java.util.Set;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class CacheInvalidationService {

	@Qualifier("cacheRedisTemplate")
	private final RedisTemplate<String, ?> cacheRedisTemplate;

	// 매주 월요일 오전 4시에 실행
	@Scheduled(cron = "0 0 4 * * MON")
	public void invalidateRecommendationCache() {
		try {
			log.info("[캐시 무효화] 적금 상품 추천 캐시 무효화 시작");

			// finproduct:recommendation:* 패턴의 모든 키 조회
			Set<String> keys = cacheRedisTemplate.keys("finproduct:recommendation:*");

			if (keys != null && !keys.isEmpty()) {
				// 모든 캐시 삭제
				Long deletedCount = cacheRedisTemplate.delete(keys);
				log.info("[캐시 무효화] 완료 - 삭제된 캐시 수: {}", deletedCount);
			} else {
				log.info("[캐시 무효화] 삭제할 캐시가 없습니다.");
			}
		} catch (Exception e) {
			log.error("[캐시 무효화] 실패 - 오류: {}", e.getMessage(), e);
		}
	}
}
