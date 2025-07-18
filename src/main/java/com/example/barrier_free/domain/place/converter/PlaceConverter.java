package com.example.barrier_free.domain.place.converter;

import com.example.barrier_free.domain.place.dto.PlaceDetailResponse;
import com.example.barrier_free.domain.place.dto.PlaceSummaryResponse;
import com.example.barrier_free.global.common.Place;

public class PlaceConverter {

	public static PlaceDetailResponse toPlaceDetailResponse(Place place, boolean favorite) {
		return new PlaceDetailResponse(
			place.getName(),
			place.getDescription(),
			place.getAddress(),
			place.getOpeningHours(),
			place.getFacility(),
			place.getPlaceType(),
			place.getImageType().getCode(),
			favorite
		);
	}

	public static PlaceSummaryResponse toPlaceSummaryResponse(Place place, boolean favorite) {
		return new PlaceSummaryResponse(
			place.getName(),
			place.getDescription(),
			place.getAddress(),
			place.getFacility(),
			place.getPlaceType(),
			place.getImageType().getCode(),
			favorite
		);
	}
}
