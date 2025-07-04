package com.example.barrier_free.domain.review.dto;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import com.example.barrier_free.domain.review.entity.Review;
import com.example.barrier_free.global.infra.S3Service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlaceReviewPageResponse {
	private List<PlaceReviewResponse> reviews;
	private int page;
	private int size;
	private int totalPages;
	private long totalElements;
	private boolean hasNext;

	public static PlaceReviewPageResponse from(Page<Review> reviewPage, S3Service s3Service) {
		List<PlaceReviewResponse> reviewDtos = reviewPage.getContent().stream()
			.map(review -> PlaceReviewResponse.from(review, s3Service))
			.collect(Collectors.toList());

		return new PlaceReviewPageResponse(
			reviewDtos,
			reviewPage.getNumber(),
			reviewPage.getSize(),
			reviewPage.getTotalPages(),
			reviewPage.getTotalElements(),
			reviewPage.hasNext()
		);
	}
}
