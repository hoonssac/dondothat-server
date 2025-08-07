package org.bbagisix.challenge.scheduler;

import org.bbagisix.challenge.service.ChallengeService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChallengeScheduler {
	private final ChallengeService challengeService;

	@Scheduled(cron = "0 59 23 * * *")
	public void DailyCheck() {
		challengeService.dailyCheck();
	}

}
