package com.example.barrier_free.global.response;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum SuccessCode {
	OK(HttpStatus.OK, "2000", "성공했습니다."),
	LOGIN_SUCCESSFUL(HttpStatus.OK, "USER2002", "로그인에 성공했습니다."),
	CREATED(HttpStatus.CREATED, "2010", "생성되었습니다."),
	REVIEW_CREATED(HttpStatus.CREATED, "REVIEW2010", "리뷰작성 완료"),
	// 이 아래로 추가하고 싶은 하세요
	USER_CREATED(HttpStatus.CREATED, "USER2001", "회원가입에 성공했습니다.");

	private final HttpStatus httpStatus;
	private final String code;
	private final String message;

}


