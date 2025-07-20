package com.example.barrier_free.domain.review.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.example.barrier_free.domain.review.entity.Review;
import com.example.barrier_free.global.infra.S3Service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlaceReviewResponse {
	private String nickname;
	private double rating;
	private String content;
	private List<String> imageUrls;

	public static PlaceReviewResponse from(Review review, S3Service s3Service) {
		return new PlaceReviewResponse(
			review.getUser().getNickname(),
			review.getRating(),
			review.getContent(),
			review.getReviewImages().stream()
				.map(image -> s3Service.getFullUrl(image.getUrl()))
				.collect(Collectors.toList())
		);
	}
}
