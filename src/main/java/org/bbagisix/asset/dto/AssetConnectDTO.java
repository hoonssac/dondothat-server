package org.bbagisix.asset.dto;

import lombok.Data;

/*사용자가 은행 계좌를 서비스에 연동할 떄 사용하는 DTO*/
@Data
public class AssetConnectDTO {
	private String bankName;
	private String bankAccount;
	private String bankId;
	private String bankPw;
}
