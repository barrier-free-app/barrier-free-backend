package com.example.barrier_free.domain.review.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ReviewRequestDto {
	@NotNull
	private String content;
	@NotNull
	private double rating;
}
