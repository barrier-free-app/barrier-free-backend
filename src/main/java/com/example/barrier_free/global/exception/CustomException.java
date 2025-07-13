package com.example.barrier_free.global.exception;

import com.example.barrier_free.global.response.ErrorCode;
import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

	private final ErrorCode errorCode;
	private final String customMessage;	// 커스텀 메시지

	public ErrorCode getErrorcode() {
		return errorCode;
	}

	public HttpStatus getHttpStatus() {
		return errorCode.getHttpStatus();
	}

	public String getCode() {
		return errorCode.getCode();
	}

	public String getMessage() {
		return customMessage != null ? customMessage : errorCode.getMessage();
	}

	// 생성자
	// 기본
	public CustomException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
		this.customMessage = null;
	}

	// 커스텀(동적)
	public CustomException(ErrorCode errorCode, String customMessage) {
		super(customMessage);
		this.errorCode = errorCode;
		this.customMessage = customMessage;
	}
}