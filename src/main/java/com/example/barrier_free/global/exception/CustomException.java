package com.example.barrier_free.global.exception;

import com.example.barrier_free.global.response.ErrorCode;
import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomException extends RuntimeException {

	private final ErrorCode errorCode;

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
		return errorCode.getMessage();
	}
}