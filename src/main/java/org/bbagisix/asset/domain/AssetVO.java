package org.bbagisix.asset.domain;

import java.util.Date;

import lombok.Data;

@Data
public class AssetVO {
	private Long assetId;
	private Long userId;
	private String assetName;
	private Long balance;
	private String bankName;
	private String bankAccount;
	private String bankId;
	private String bankPw;
	private Date createdAt;
}
