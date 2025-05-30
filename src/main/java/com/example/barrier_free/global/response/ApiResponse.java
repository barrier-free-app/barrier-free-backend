package com.example.barrier_free.global.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.barrier_free.global.exception.CustomException;
import com.example.barrier_free.global.exception.dto.ExceptionDto;

import io.micrometer.common.lang.Nullable;

public record ApiResponse<T>(
	HttpStatus httpStatus,
	boolean success,
	@Nullable T result,
	@Nullable ExceptionDto error) {

	public static <T> ResponseEntity<ApiResponse<T>> noContentSuccess() {
		ApiResponse<T> response = new ApiResponse<>(HttpStatus.NO_CONTENT, true, null, null);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
	}

	public static <T> ResponseEntity<ApiResponse<T>> loginSuccess(HttpStatus httpStatus, @Nullable final T result,
		String accessToken) {
		ApiResponse<T> response = new ApiResponse<>(httpStatus, true, result, null);
		return ResponseEntity.ok()
			.header("Authorization", "Bearer " + accessToken)
			.body(response);
	}

	public static <T> ResponseEntity<ApiResponse<T>> success(HttpStatus httpStatus, @Nullable final T result) {
		ApiResponse<T> response = new ApiResponse<>(httpStatus, true, result, null);
		return new ResponseEntity<>(response, httpStatus);
	}

	public static <T> ApiResponse<T> fail(final CustomException e, @Nullable final T result) {
		return new ApiResponse<>(e.getHttpStatus(), false, result, ExceptionDto.of(e.getErrorCode()));
	}

}
