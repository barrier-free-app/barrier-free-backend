package com.example.barrier_free.global.response;

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
	// TODO: 이름 바꾸기
	INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "JWT4001", "유효하지 않은 JWT 토큰입니다."),
	INVALID_SIGNATURE(HttpStatus.UNAUTHORIZED, "JWT4002", "JWT 서명이 유효하지 않습니다."),
	TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "JWT4003", "JWT 토큰이 만료되었습니다."),
	MISSING_TOKEN(HttpStatus.UNAUTHORIZED, "JWT4000", "JWT 토큰이 요청에 포함되어 있지 않습니다."),
	JWT_MALFORMED(HttpStatus.UNAUTHORIZED, "JWT4002", "JWT 토큰의 형식이 잘못되었습니다."), // 형식 오류
	UNSUPPORTED_JWT(HttpStatus.UNAUTHORIZED, "JWT4004", "지원하지 않는 JWT 토큰입니다."),
	EMPTY_JWT(HttpStatus.UNAUTHORIZED, "JWT4005", "JWT 클레임이 비어 있습니다."),

	NOT_FOUND_FACILITY(HttpStatus.BAD_REQUEST, "FACILITY400", "존재하지 않는 편의시설입니다."),

	JWT_INVALID(HttpStatus.UNAUTHORIZED, "JWT4001", "유효하지 않은 JWT 토큰입니다."),
	JWT_EXPIRED(HttpStatus.UNAUTHORIZED, "JWT4002", "JWT 토큰이 만료되었습니다."),
	JWT_NOT_FOUND(HttpStatus.UNAUTHORIZED, "JWT4003", "Authorization 헤더에 토큰이 존재하지 않습니다."),
	JWT_UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "JWT5001", "토큰 처리 중 예기치 않은 오류가 발생했습니다."),

	S3_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "S3_5001", "파일 업로드 중 오류가 발생했습니다."),
	S3_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "S3_5002", "파일 삭제 중 오류가 발생했습니다."),
	PLACE_NOT_FOUND(HttpStatus.BAD_REQUEST, "PLACE_404", "해당 장소를 찾을 수 없음"),
	REVIEW_NOT_FOUND(HttpStatus.BAD_REQUEST, "REVIEW_404", "해당 리뷰를 찾을 수 없음"),
	ALREADY_VOTE(HttpStatus.BAD_REQUEST, "VOTE2001", "이미 투표했습니다."),

	NOT_FOUND_REPORT(HttpStatus.NOT_FOUND, "REPORT4041", "존재하지 않는 제보장소입니다."),

	NOT_FOUND_USER(HttpStatus.NOT_FOUND, "USER4041", "존재하지 않는 사용자입니다."),

	INVALID_REDISKEY(HttpStatus.BAD_REQUEST, "REDIS4001", "유효하지 않은 키 형식입니다."),
	INVALID_WEEKLY_RANK(HttpStatus.INTERNAL_SERVER_ERROR, "WEEKLY5001", "주간 랭킹에 장소 정보가 없습니다.");

	private final HttpStatus httpStatus;
	private final String code;
	private final String message;

}


