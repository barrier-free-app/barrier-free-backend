package com.example.barrier_free.global.exception.dto;

import org.antlr.v4.runtime.misc.NotNull;

import com.example.barrier_free.global.exception.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExceptionDto {
	@NotNull
	private final String code;

	@NotNull
	private final String message;

	public ExceptionDto(ErrorCode errorCode) {
		this.code = errorCode.getCode();
		this.message = errorCode.getMessage();
	}

	public static ExceptionDto of(ErrorCode errorCode) {
		return new ExceptionDto(errorCode);
	}
}
