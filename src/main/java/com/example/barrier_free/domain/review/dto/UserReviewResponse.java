package com.example.barrier_free.domain.review.dto;

import java.util.List;
import java.util.Optional;

import com.example.barrier_free.domain.review.entity.Review;
import com.example.barrier_free.global.common.PlaceType;
import com.example.barrier_free.global.exception.CustomException;
import com.example.barrier_free.global.infra.S3Service;
import com.example.barrier_free.global.response.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserReviewResponse {

	private Long placeId;
	private String placeName;
	private PlaceType placeType; //
	// private Category category; // 나중에 사용 가능
	private String content;
	private float rating;
	private List<String> reviewImageUrls;

	public static UserReviewResponse from(Review review, S3Service s3Service) {
		List<String> reviewImageUrls = Optional.ofNullable(review.getReviewImages())
			.orElse(List.of())
			.stream()
			.filter(img -> img.getUrl() != null)
			.map(img -> s3Service.getFullUrl(img.getUrl()))
			.toList();

		if (review.getMap() != null) {
			return new UserReviewResponse(
				review.getMap().getId(),
				review.getMap().getName(),
				PlaceType.map,
				review.getContent(),
				review.getRating(),
				reviewImageUrls
			);
		} else if (review.getReport() != null) {
			return new UserReviewResponse(
				review.getReport().getId(),
				review.getReport().getName(),
				PlaceType.report,
				review.getContent(),
				review.getRating(),
				reviewImageUrls
			);
		} else {
			throw new CustomException(ErrorCode.INVALID_REVIEW);
		}
	}

}
