package com.example.barrier_free.global.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

	_INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
	_BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
	_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
	_FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),
	_NOT_FOUND_END_POINT(HttpStatus.NOT_FOUND, "COMMON40400", "존재하지 않는 API입니다."),
	_NOT_FOUND_RESOURCE(HttpStatus.NOT_FOUND, "COMMON40401", "요청한 리소스를 찾을 수 없습니다."),
	_METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "COMMON405", "허용되지 않은 요청 방식입니다."),
	_TOO_MANY_REQUESTS(HttpStatus.TOO_MANY_REQUESTS, "COMMON429", "너무 많은 요청입니다. 잠시 후 다시 시도해주세요."),

	/*에러 추가 가능*/
	USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "USER4001", "존재하지 않는 사용자 ID입니다."),
	USER_NOT_AUTHORIZED(HttpStatus.UNAUTHORIZED, "USER4002", "사용자 인증이 필요합니다."),
	NOT_FOUND_AUTHORIZATION(HttpStatus.UNAUTHORIZED, "USER4003", "Authorization 오류가 있습니다."),
	DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "USER4006", "중복된 이메일입니다"),
	LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "USER4011", "로그인 정보가 잘못되었습니다."),

	/* JWT 관련 에러 */
	INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "JWT4001", "유효하지 않은 JWT 토큰입니다."),
	INVALID_SIGNATURE(HttpStatus.UNAUTHORIZED, "JWT4002", "JWT 서명이 유효하지 않습니다."),
	TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "JWT4003", "JWT 토큰이 만료되었습니다."),
	MISSING_TOKEN(HttpStatus.UNAUTHORIZED, "JWT4000", "JWT 토큰이 요청에 포함되어 있지 않습니다."),
	JWT_MALFORMED(HttpStatus.UNAUTHORIZED, "JWT4002", "JWT 토큰의 형식이 잘못되었습니다."), // 형식 오류
	UNSUPPORTED_JWT(HttpStatus.UNAUTHORIZED, "JWT4004", "지원하지 않는 JWT 토큰입니다."),
	EMPTY_JWT(HttpStatus.UNAUTHORIZED, "JWT4005", "JWT 클레임이 비어 있습니다.");

	private final HttpStatus httpStatus;
	private final String code;
	private final String message;

}


