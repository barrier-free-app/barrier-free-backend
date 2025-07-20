package com.example.barrier_free.domain.place.converter;

import com.example.barrier_free.domain.place.dto.PlaceDetailResponse;
import com.example.barrier_free.domain.place.dto.PlaceSummaryResponse;
import com.example.barrier_free.global.common.Place;

public class PlaceConverter {

	public static PlaceDetailResponse toPlaceDetailResponse(Place place, boolean favorite) {
		int reviewCount = place.getReviewCount();
		double avgRating = reviewCount == 0 ? 0.0 :
			Math.round((place.getRatingSum() / reviewCount) * 10.0) / 10.0;

		return new PlaceDetailResponse(
			place.getName(),
			place.getDescription(),
			place.getAddress(),
			place.getOpeningHours(),
			place.getFacility(),
			place.getImageType().getCode(),
			favorite,
			reviewCount,
			avgRating


		);
	}

	public static PlaceSummaryResponse toPlaceSummaryResponse(Place place, boolean favorite) {
		return new PlaceSummaryResponse(
			place.getName(),
			place.getDescription(),
			place.getAddress(),
			place.getFacility(),
			place.getImageType().getCode(),
			favorite
		);
	}
}
