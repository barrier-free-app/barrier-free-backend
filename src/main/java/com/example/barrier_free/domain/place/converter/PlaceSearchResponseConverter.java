package com.example.barrier_free.domain.place.converter;

import java.util.List;
import java.util.Map;

import com.example.barrier_free.domain.place.dto.PlaceSearchResponse;
import com.example.barrier_free.domain.place.entity.PlaceView;
import com.example.barrier_free.global.common.PlaceType;

public class PlaceSearchResponseConverter {
	public static PlaceSearchResponse from(PlaceView placeView, Map<Long, List<Integer>> mapFacilities,
		Map<Long, List<Integer>> reportFacilities) {

		Long placeId = placeView.getId();
		PlaceType placeType = placeView.getPlaceType();
		String name = placeView.getName();
		String region = placeView.getRegion();
		int imageType = placeView.getImageType().getCode();

		List<Integer> facilities;
		if (placeType == PlaceType.map) {
			facilities = mapFacilities.getOrDefault(placeId, List.of());
		} else {
			facilities = reportFacilities.getOrDefault(placeId, List.of());
		}

		return new PlaceSearchResponse(placeId, placeType, facilities, region, name, imageType);

	}
}
