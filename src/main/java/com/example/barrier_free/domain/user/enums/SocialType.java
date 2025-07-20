package com.example.barrier_free.domain.user.enums;

import com.example.barrier_free.global.exception.CustomException;
import com.example.barrier_free.global.response.ErrorCode;

public enum SocialType {
	GENERAL, // 일반 로그인
	KAKAO,
	NAVER;

	public static SocialType fromString(String value) {
		try {
			return SocialType.valueOf(value.toUpperCase());
		} catch (Exception e) {
			throw new CustomException(ErrorCode.USER_SOCIAL_TYPE_NOT_FOUND);
		}
	}
}
