package org.bbagisix.challenge.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChallengeProgressDTO {
	private Long challenge_id;
	private String title;
	private Long period;
	private String status;
	private Long progress;
	private Long saving;
	private Boolean is_active;
}