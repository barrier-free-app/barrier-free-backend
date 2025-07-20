package com.example.barrier_free.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NicknameCheckResponse {
	private boolean available;
}
