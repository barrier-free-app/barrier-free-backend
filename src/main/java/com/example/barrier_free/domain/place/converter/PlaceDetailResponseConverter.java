package com.example.barrier_free.domain.place.converter;

import com.example.barrier_free.domain.place.dto.PlaceDetailResponse;
import com.example.barrier_free.global.common.Place;

public class PlaceDetailResponseConverter {
	public static PlaceDetailResponse from(Place place, boolean favorite) {
		return new PlaceDetailResponse(
			place.getName(),
			place.getDescription(),
			place.getAddress(),
			place.getOpeningHours(),
			place.getFacility(),
			place.getPlaceType(),
			favorite
		);
	}
}
