package org.bbagisix.mypage.service;

import org.bbagisix.asset.domain.AssetVO;
import org.bbagisix.asset.mapper.AssetMapper;
import org.bbagisix.exception.BusinessException;
import org.bbagisix.exception.ErrorCode;
import org.bbagisix.mypage.domain.MyPageDTO;
import org.bbagisix.tier.service.TierService;
import org.bbagisix.tier.dto.TierDTO;
import org.bbagisix.user.dto.CustomOAuth2User;
import org.bbagisix.user.mapper.UserMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class MyPageService {

	private final AssetMapper assetMapper;
	private final UserMapper userMapper;
	private final TierService tierService;

	public MyPageDTO getUserAccountData(Authentication authentication) {
		Long userId = extractUserId(authentication);

		// main 계좌 조회
		AssetVO mainAsset = assetMapper.selectAssetByUserIdAndStatus(userId, "main");

		// sub 계좌 조회
		AssetVO subAsset = assetMapper.selectAssetByUserIdAndStatus(userId, "sub");

		// tier 정보 조회
		MyPageDTO.TierInfo tierInfo = getUserTierInfo(authentication);

		return MyPageDTO.builder()
			.mainAccount(mainAsset != null ? convertToAccountInfo(mainAsset) : null)
			.subAccount(subAsset != null ? convertToAccountInfo(subAsset) : null)
			.tierInfo(tierInfo)
			.build();
	}

	public MyPageDTO.TierInfo getUserTierInfo(Authentication authentication) {
		Long userId = extractUserId(authentication);

		// 현재 티어 정보 조회
		TierDTO currentTier = tierService.getUserCurrentTier(userId);

		// 완료한 챌린지 수 조회
		Integer completedChallenges = getCompletedChallengeCount(userId);

		if (currentTier == null) {
			// 티어가 없는 경우 기본값 반환
			return MyPageDTO.TierInfo.builder()
				.tierId(null)
				.tierName("티어 없음")
				.completedChallenges(completedChallenges)
				.build();
		}

		return MyPageDTO.TierInfo.builder()
			.tierId(currentTier.getTierId())
			.tierName(currentTier.getName())
			.completedChallenges(completedChallenges)
			.build();
	}

	// Authentication에서 사용자 ID 추출
	private Long extractUserId(Authentication authentication) {
		if (authentication == null || authentication.getPrincipal() == null) {
			throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
		}

		CustomOAuth2User curUser = (CustomOAuth2User)authentication.getPrincipal();
		if (curUser == null || curUser.getUserId() == null) {
			throw new BusinessException(ErrorCode.USER_ID_REQUIRED);
		}

		return curUser.getUserId();
	}

	private MyPageDTO.AccountInfo convertToAccountInfo(AssetVO assetVO) {
		return MyPageDTO.AccountInfo.builder()
			.assetId(assetVO.getAssetId())
			.assetName(assetVO.getAssetName())
			.bankName(assetVO.getBankName())
			.balance(assetVO.getBalance())
			.status(assetVO.getStatus())
			.build();
	}

	private Integer getCompletedChallengeCount(Long userId) {
		Integer count = userMapper.getCompletedChallengeCount(userId);
		return count != null ? count : 0;
	}
}