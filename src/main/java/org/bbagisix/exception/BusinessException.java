package org.bbagisix.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

	private final ErrorCode errorCode;

	public BusinessException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}

	public BusinessException(ErrorCode errorCode, String customMessage) {
		super(customMessage);
		this.errorCode = errorCode;
	}

	public BusinessException(ErrorCode errorCode, Throwable cause) {
		super(errorCode.getMessage(), cause);
		this.errorCode = errorCode;
	}

	public BusinessException(ErrorCode errorCode, String customMessage, Throwable cause) {
		super(customMessage, cause);
		this.errorCode = errorCode;
	}

	public String getCode() {
		return errorCode.getCode();
	}

	public String getErrorMessage() {
		return errorCode.getMessage();
	}

	public org.springframework.http.HttpStatus getHttpStatus() {
		return errorCode.getHttpStatus();
	}
}
