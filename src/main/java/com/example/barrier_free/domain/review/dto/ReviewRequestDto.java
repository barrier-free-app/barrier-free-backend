package com.example.barrier_free.domain.review.dto;

import lombok.Getter;

@Getter
public class ReviewRequestDto {
	private String content;
	private int rating;
	private Long userId;
}
