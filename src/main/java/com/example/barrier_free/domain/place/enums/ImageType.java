package com.example.barrier_free.domain.place.enums;

import com.example.barrier_free.global.exception.CustomException;
import com.example.barrier_free.global.response.ErrorCode;

public enum ImageType {
	PARKING(0),
	CULTURAL(1),
	RESTAURANT(2),
	ELEVATOR(3),
	NURSING(4),
	TOILET(5);

	private final int code;

	ImageType(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public static ImageType fromCode(int code) {
		for (ImageType type : values()) {
			if (type.code == code) {
				return type;
			}
		}
		throw new CustomException(ErrorCode.INVALID_IMAGE_TYPE);
	}
}
