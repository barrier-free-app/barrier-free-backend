package com.example.barrier_free.domain.place.converter;

import com.example.barrier_free.domain.place.dto.PlaceSummaryResponse;
import com.example.barrier_free.global.common.Place;

public class PlaceSummaryResponseConverter {
	public static PlaceSummaryResponse from(Place place, boolean favorite) {
		return new PlaceSummaryResponse(
			place.getName(),
			place.getDescription(),
			place.getAddress(),
			place.getFacility(),
			place.getPlaceType(),
			favorite
		);
	}
}
