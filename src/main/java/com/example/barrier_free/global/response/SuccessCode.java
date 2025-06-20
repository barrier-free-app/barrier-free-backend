package com.example.barrier_free.global.response;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SuccessCode {
	OK(HttpStatus.OK, "2000", "성공했습니다."),
	LOGIN_SUCCESSFUL(HttpStatus.OK, "USER2001", "로그인에 성공했습니다."),
	CREATED(HttpStatus.CREATED, "2010", "생성되었습니다.");
	// 이 아래로 추가하고 싶은 하세요

	private final HttpStatus httpStatus;
	private final String code;
	private final String message;

}


