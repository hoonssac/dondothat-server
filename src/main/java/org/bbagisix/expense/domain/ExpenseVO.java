package org.bbagisix.expense.domain;

import java.util.Date;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ExpenseVO {
	private Long expenditureId;
	private Long userId;
	private Long categoryId;
	private Long assetId;
	private Long amount;
	private String description;
	private Date expenditureDate;
	private Date createdAt;
	private Date updatedAt;
}
