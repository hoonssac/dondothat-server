package org.bbagisix.saving.mapper;

import java.util.List;

import org.bbagisix.saving.domain.UserChallengeVO;

public interface SavingMapper {
	List<UserChallengeVO> getSavingHistory(Long userId);

	Long getTotalSaving(Long userId);
}
