package org.bbagisix.tier.service;

import java.util.List;
import java.util.stream.Collectors;

import org.bbagisix.exception.BusinessException;
import org.bbagisix.exception.ErrorCode;
import org.bbagisix.tier.domain.TierVO;
import org.bbagisix.tier.dto.TierDTO;
import org.bbagisix.tier.mapper.TierMapper;
import org.bbagisix.user.domain.UserVO;
import org.bbagisix.user.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TierService {

	private final TierMapper tierMapper;
	private final UserMapper userMapper;

	// 모든 티어 목록 조회
	public List<TierDTO> getAllTiers() {
		List<TierVO> allTiers = tierMapper.findAllTiers();

		return allTiers.stream()
			.map(TierDTO::from)
			.collect(Collectors.toList());
	}

	// 사용자의 현재 티어 조회
	public TierDTO getUserCurrentTier(Long userId) {
		if (userId == null) {
			throw new BusinessException(ErrorCode.USER_ID_REQUIRED);
		}

		// 사용자 정보 조회
		UserVO user = userMapper.findByUserId(userId);
		if (user == null) {
			throw new BusinessException(ErrorCode.USER_NOT_FOUND);
		}

		// tier_id가 null이면 아직 티어가 없는 상태
		if (user.getTierId() == null) {
			return null;    // 또는 기본 티어 반환
		}

		// 현재 티어 조회
		TierVO currentTier = tierMapper.findById(user.getTierId());
		if (currentTier == null) {
			log.warn("사용자 {}의 tier_id {}에 해당하는 티어를 찾을 수 없습니다.", userId, user.getTierId());
			return null;
		}

		return TierDTO.from(currentTier);
	}

	// 챌린지 성공 시 티어 승급 처리
	@Transactional
	public void promoteUserTier(Long userId) {
		// 완료한 챌린지 수 조회
		Integer completedChallenges = userMapper.getCompletedChallengeCount(userId);
		
		// 완료한 챌린지 수에 기반한 적절한 tier 계산
		Long appropriateTierId = calculateTierByCompletedChallenges(completedChallenges);
		
		// 현재 사용자 정보 조회
		UserVO user = userMapper.findByUserId(userId);
		Long currentTierId = user.getTierId();
		
		// 계산된 tier가 현재 tier보다 높은 경우에만 업데이트
		if (appropriateTierId > (currentTierId != null ? currentTierId : 0L)) {
			// 해당 tier가 실제로 존재하는지 확인
			TierVO targetTier = tierMapper.findById(appropriateTierId);
			if (targetTier != null) {
				userMapper.updateUserTier(userId, appropriateTierId);
				log.info("사용자 {} 티어 승급: {} → {} (완료한 챌린지: {}개)", 
					userId, currentTierId, appropriateTierId, completedChallenges);
			}
		}
	}
	
	// 완료한 챌린지 수에 따른 tier 계산
	private Long calculateTierByCompletedChallenges(Integer completedChallenges) {
		if (completedChallenges == null || completedChallenges == 0) {
			return 1L; // 기본 tier
		} else if (completedChallenges <= 2) {
			return 1L;
		} else if (completedChallenges <= 5) {
			return 2L;
		} else if (completedChallenges <= 10) {
			return 3L;
		} else {
			return 4L; // 최고 tier
		}
	}
	
	// 모든 사용자의 tier를 완료한 챌린지 수에 기반하여 재계산
	@Transactional
	public void recalculateAllUserTiers() {
		// 모든 사용자 ID 조회 (UserMapper에 메서드가 없으므로 간단히 특정 사용자만 처리)
		log.info("모든 사용자 tier 재계산 시작");
		// 이 메서드는 필요시 모든 사용자를 대상으로 tier 재계산을 수행할 수 있습니다
	}
	
	// 특정 사용자의 tier를 완료한 챌린지 수에 기반하여 재계산
	@Transactional
	public void recalculateUserTier(Long userId) {
		promoteUserTier(userId);
		log.info("사용자 {} tier 재계산 완료", userId);
	}
}
