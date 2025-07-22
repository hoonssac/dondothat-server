package org.bbagisix.asset.dto;

import java.util.Date;

import lombok.Data;

/* API를 통해 클라이언트에게 보여지는 DTO
 * ID와 PW 제외
 * */
@Data
public class AssetDTO {
	private Long assetId;
	private String assetName;
	private Long balance;
	private String bankName;
	private String bankAccount;
	private Date createdAt;
}
