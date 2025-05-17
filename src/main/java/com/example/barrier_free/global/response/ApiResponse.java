package com.example.barrier_free.global.response;

import com.example.barrier_free.global.exception.ErrorCode;
import com.example.barrier_free.global.exception.SuccessCode;

import io.micrometer.common.lang.Nullable;
import org.springframework.http.HttpStatus;

public record ApiResponse<T>(
	boolean isSuccess,
	HttpStatus httpStatus,
	String code,
	String message,
	@Nullable T result) {

//	public static <T> ResponseEntity<ApiResponse<T>> noContentSuccess() {
//		ApiResponse<T> response = new ApiResponse<>(HttpStatus.NO_CONTENT, true, null, null);
//		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
//	}

//	public static <T> ResponseEntity<ApiResponse<T>> loginSuccess(HttpStatus httpStatus, @Nullable final T result,
//		String accessToken) {
//		ApiResponse<T> response = new ApiResponse<>(httpStatus, true, result, null);
//		return ResponseEntity.ok()
//			.header("Authorization", "Bearer " + accessToken)
//			.body(response);
//	}

	public static <T> ApiResponse<T> success(SuccessCode code, @Nullable final T result) {
		return new ApiResponse<>(true, code.getHttpStatus(), code.getCode(), code.getMessage(), result);
	}

	public static <T> ApiResponse<T> fail(ErrorCode code) {
		return new ApiResponse<>(false, code.getHttpStatus(), code.getCode(), code.getMessage(), null);
	}

}
