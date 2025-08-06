package org.bbagisix.saving.service;

import java.util.List;
import java.util.stream.Collectors;

import org.bbagisix.challenge.domain.ChallengeVO;
import org.bbagisix.challenge.mapper.ChallengeMapper;
import org.bbagisix.exception.BusinessException;
import org.bbagisix.exception.ErrorCode;
import org.bbagisix.saving.domain.UserChallengeVO;
import org.bbagisix.saving.dto.SavingDTO;
import org.bbagisix.saving.mapper.SavingMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class SavingService {

	private final SavingMapper savingMapper;
	private final ChallengeMapper challengeMapper;

	public List<SavingDTO> getSavingHistory(Long userId) {
		try {
			return savingMapper.getSavingHistory(userId).stream().map(this::voToDto).collect(Collectors.toList());
		} catch (Exception e) {
			log.error("사용자 저금 내역 조회 중 오류 발생: userId={}, error={}", userId, e.getMessage(), e);
			throw new BusinessException(ErrorCode.DATA_ACCESS_ERROR, e);
		}
	}

	public Long getTotalSaving(Long userId) {
		try {
			return savingMapper.getTotalSaving(userId);
		} catch (Exception e) {
			log.error("사용자 총 저금액 조회 중 오류 발생: userId={}, error={}", userId, e.getMessage(), e);
			throw new BusinessException(ErrorCode.DATA_ACCESS_ERROR, e);
		}
	}

	private SavingDTO voToDto(UserChallengeVO userChallengeVO) {
		if (userChallengeVO == null)
			return null;
		ChallengeVO challengeVO = challengeMapper.findByChallengeId(userChallengeVO.getChallengeId());
		return SavingDTO.builder()
			.categoryId(challengeVO.getCategoryId())
			.title(challengeVO.getTitle())
			.startDate(userChallengeVO.getStartDate())
			.endDate(userChallengeVO.getEndDate())
			.saving(userChallengeVO.getSaving())
			.build();
	}

}
