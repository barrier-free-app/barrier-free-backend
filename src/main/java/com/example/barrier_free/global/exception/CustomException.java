package com.example.barrier_free.global.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomException extends RuntimeException {

	private final ErrorCode errorCode;

	public String getMessage() {
		return errorCode.getMessage();
	}

	public HttpStatus getHttpStatus() {
		return errorCode.getHttpStatus();
	}

}