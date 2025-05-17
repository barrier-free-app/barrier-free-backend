package com.example.barrier_free.global.exception.handler;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.example.barrier_free.global.exception.CustomException;
import com.example.barrier_free.global.exception.ErrorCode;
import com.example.barrier_free.global.response.ApiResponse;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	// 존재하지 않는 요청
	@ExceptionHandler(value = {NoHandlerFoundException.class})
	public ResponseEntity<ApiResponse<?>> handleNoPageFoundException(Exception e) {
		log.error("NoHandlerFoundException: {}", e.getMessage());
		return buildResponse(ErrorCode._NOT_FOUND_END_POINT);
	}

	// 검증 실패 (RequestParam 유효성 검증)
	@ExceptionHandler(value = {ConstraintViolationException.class})
	public ResponseEntity<ApiResponse<?>> handleValidationException(ConstraintViolationException e) {
		String errorMessage = e.getConstraintViolations().stream()
			.map(ConstraintViolation::getMessage)
			.findFirst()
			.orElse("Validation error occurred");
		log.error("Validation error: {}", errorMessage);
		return buildResponse(ErrorCode._BAD_REQUEST);
	}

	// 검증 실패 (RequestBody 유효성 검증)
	@ExceptionHandler(value = {MethodArgumentNotValidException.class})
	public ResponseEntity<ApiResponse<?>> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
		Map<String, String> errors = new LinkedHashMap<>();
		e.getBindingResult().getFieldErrors()
			.forEach(fieldError -> errors.put(fieldError.getField(), fieldError.getDefaultMessage()));
		log.error("MethodArgumentNotValidException: {}", errors);
		return buildResponse(ErrorCode._BAD_REQUEST);
	}

	// 커스텀 예외
	@ExceptionHandler(value = {CustomException.class})
	public ResponseEntity<ApiResponse<?>> handleCustomException(CustomException e) {
		log.error("CustomException: {}", e.getMessage());
		return buildResponse(e.getErrorcode());
	}

	@ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class})
	public ResponseEntity<ApiResponse<?>> handleHttpRequestMethodNotSupported(
		HttpRequestMethodNotSupportedException e) {
		log.warn("HttpRequestMethodNotSupportedException: {}", e.getMessage());
//		return buildResponse(ErrorCode._METHOD_NOT_ALLOWED, "허용되지 않은 HTTP 메서드입니다.");
		return buildResponse(ErrorCode._METHOD_NOT_ALLOWED);
	}

	// 기본 예외
	@ExceptionHandler(value = {Exception.class})
	public ResponseEntity<ApiResponse<?>> handleAllException(Exception e) {

		log.error("Unexpected error: {}", e.getMessage());
//		return buildResponse(ErrorCode._INTERNAL_SERVER_ERROR, e.getMessage());
		return buildResponse(ErrorCode._INTERNAL_SERVER_ERROR);
	}

	private ResponseEntity<ApiResponse<?>> buildResponse(ErrorCode errorCode) {
		return ResponseEntity
			.status(errorCode.getHttpStatus())
			.body(ApiResponse.fail(errorCode));
	}
}
