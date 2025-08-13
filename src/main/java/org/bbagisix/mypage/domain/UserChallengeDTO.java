package org.bbagisix.mypage.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserChallengeDTO {
	private Long userChallengeId;
	private String title;           // 챌린지 제목
	private String status;          // completed, failed

	// JSON 출력 시 날짜 형식 지정
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
	private Date startDate;         // 시작일

	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
	private Date endDate;           // 종료일

	private Long categoryId;        // 아이콘용
}