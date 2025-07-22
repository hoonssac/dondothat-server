package org.bbagisix.expense.dto;

import java.util.Date;

import lombok.Data;

@Data
public class ExpenseDTO {
	// 응답 필드
	private Long expenditureId;
	private String categoryName;
	private String categoryIcon;
	private Date createdAt;
	private Date updatedAt;

	// 요청/응답 공통 필드
	private Long userId;
	private Long categoryId;
	private Long assetId;
	private int amount;
	private String description;
	private Date expenditureDate;
}
