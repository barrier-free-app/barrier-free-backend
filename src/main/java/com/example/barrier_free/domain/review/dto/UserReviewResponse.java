package com.example.barrier_free.domain.review.dto;

import java.util.List;
import java.util.Optional;

import com.example.barrier_free.domain.map.entity.Map;
import com.example.barrier_free.domain.place.enums.ImageType;
import com.example.barrier_free.domain.report.entity.Report;
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
	private int imageType;
	private String content;
	private double rating;
	private List<String> reviewImageUrls;

	public static UserReviewResponse from(Review review, S3Service s3Service) {
		List<String> reviewImageUrls = Optional.ofNullable(review.getReviewImages())
			.orElse(List.of())
			.stream()
			.filter(img -> img.getUrl() != null)
			.map(img -> s3Service.getFullUrl(img.getUrl()))
			.toList();

		if (review.getMap() != null) {
			Map currentMap = review.getMap();
			ImageType imageType = currentMap.getImageType();

			return new UserReviewResponse(
				currentMap.getId(),
				currentMap.getName(),
				PlaceType.map,
				imageType != null ? imageType.getCode() : -1,
				review.getContent(),
				review.getRating(),
				reviewImageUrls
			);
		} else if (review.getReport() != null) {

			Report currentReport = review.getReport();
			ImageType imageType = currentReport.getImageType();

			return new UserReviewResponse(
				currentReport.getId(),
				currentReport.getName(),
				PlaceType.report,
				imageType != null ? imageType.getCode() : -1,
				review.getContent(),
				review.getRating(),
				reviewImageUrls
			);
		} else {
			throw new CustomException(ErrorCode.INVALID_REVIEW);
		}
	}

}
