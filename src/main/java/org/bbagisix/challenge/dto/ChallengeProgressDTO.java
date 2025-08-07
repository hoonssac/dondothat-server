package org.bbagisix.challenge.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChallengeProgressDTO {
	private Long challenge_id;
	private String title;
	private Long period;        // Integer → Long
	private String status;
	private Long progress;      // Integer → Long
	private Long saving;        // Integer → Long
	private Boolean is_active;
}