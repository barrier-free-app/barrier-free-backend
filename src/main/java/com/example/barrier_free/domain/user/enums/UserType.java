package com.example.barrier_free.domain.user.enums;

import com.example.barrier_free.global.exception.CustomException;
import com.example.barrier_free.global.response.ErrorCode;

public enum UserType {
	ALL, DISABLED, PREGNANT;

	public static UserType fromString(String value) {
		try {
			return UserType.valueOf(value.toUpperCase());
		} catch (Exception e) {
			throw new CustomException(ErrorCode.USER_TYPE_NOT_FOUND);
		}
	}
}
