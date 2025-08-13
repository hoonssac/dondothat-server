package org.bbagisix.mypage.service;

import java.util.List;
import java.util.stream.Collectors;

import org.bbagisix.asset.domain.AssetVO;
import org.bbagisix.asset.mapper.AssetMapper;
import org.bbagisix.challenge.domain.ChallengeVO;
import org.bbagisix.challenge.domain.UserChallengeVO;
import org.bbagisix.challenge.mapper.ChallengeMapper;
import org.bbagisix.exception.BusinessException;
import org.bbagisix.exception.ErrorCode;
import org.bbagisix.mypage.domain.MyPageAccountDTO;
import org.bbagisix.mypage.domain.UserChallengeDTO;
import org.bbagisix.user.dto.CustomOAuth2User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class MyPageService {

	private final AssetMapper assetMapper;
	private final ChallengeMapper challengeMapper;

	public MyPageAccountDTO getUserAccountData(Authentication authentication) {
		Long userId = extractUserId(authentication);

		// main 계좌 조회
		AssetVO mainAsset = assetMapper.selectAssetByUserIdAndStatus(userId, "main");

		// sub 계좌 조회
		AssetVO subAsset = assetMapper.selectAssetByUserIdAndStatus(userId, "sub");

		return MyPageAccountDTO.builder()
			.mainAccount(mainAsset != null ? convertToAccountInfo(mainAsset) : null)
			.subAccount(subAsset != null ? convertToAccountInfo(subAsset) : null)
			.build();
	}

	/**
	 * 마이페이지 - 완료된(complete/failed) 사용자 챌린지 목록 조회
	 * - challengeMapper.getUserCompletedChallenges(userId)
	 *   결과(UserChallengeVO)를 UserChallengeDTO로 변환하여 반환
	 */
	public List<UserChallengeDTO> getUserChallenges(Authentication authentication) {
		Long userId = extractUserId(authentication);

		// 완료된 챌린지들 조회 (completed + failed)
		List<UserChallengeVO> userChallenges = challengeMapper.getUserCompletedChallenges(userId);

		return userChallenges.stream()
			.map(this::convertToUserChallengeDTO)
			.collect(Collectors.toList());
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

	private MyPageAccountDTO.AccountInfo convertToAccountInfo(AssetVO assetVO) {
		return MyPageAccountDTO.AccountInfo.builder()
			.assetId(assetVO.getAssetId())
			.assetName(assetVO.getAssetName())
			.bankName(assetVO.getBankName())
			.balance(assetVO.getBalance())
			.status(assetVO.getStatus())
			.build();
	}

	private UserChallengeDTO convertToUserChallengeDTO(UserChallengeVO userChallenge) {
		// 챌린지 상세 정보 조회
		ChallengeVO challenge = challengeMapper.findByChallengeId(userChallenge.getChallengeId());
		Long categoryId = challengeMapper.getCategoryByChallengeId(userChallenge.getChallengeId());

		return UserChallengeDTO.builder()
			.userChallengeId(userChallenge.getUserChallengeId())
			.title(challenge != null ? challenge.getTitle() : "알 수 없는 챌린지")
			.status(userChallenge.getStatus())
			.startDate(userChallenge.getStartDate())
			.endDate(userChallenge.getEndDate())
			.categoryId(categoryId)
			.build();
	}
}