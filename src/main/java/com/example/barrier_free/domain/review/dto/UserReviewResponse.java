package com.example.barrier_free.domain.review.dto;

import java.util.List;

import com.example.barrier_free.domain.review.entity.Review;
import com.example.barrier_free.global.common.Place;
import com.example.barrier_free.global.common.PlaceType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserReviewResponse {
	private Long reviewId;
	private String placeName;
	private PlaceType placeType;
	private List<Integer> facilities;
	private float rating;
	private String content;
	private String imageUrl;

	static UserReviewResponse from(Review review, Place place) {
		return new UserReviewResponse(
			review.getId(),
			place.getName(),
			place.getPlaceType(),
			place.getFacilites(),
			review.getRating(),
			review.getContent(),
			review.getReviewImages()

		);
	}

}
