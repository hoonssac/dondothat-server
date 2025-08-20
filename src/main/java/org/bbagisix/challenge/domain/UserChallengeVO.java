package org.bbagisix.challenge.domain;

import java.util.Date;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UserChallengeVO {
	private Long userChallengeId;
	private Long userId;
	private Long challengeId;
	private String status;
	private Long period;
	private Long progress;
	private Date startDate;
	private Date endDate;
	private Long saving;
	private Boolean isActive;
}
