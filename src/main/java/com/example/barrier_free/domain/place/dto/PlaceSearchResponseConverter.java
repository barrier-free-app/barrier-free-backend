package com.example.barrier_free.domain.place.dto;

import java.util.List;
import java.util.Map;

import com.example.barrier_free.domain.place.entity.PlaceView;
import com.example.barrier_free.global.common.PlaceType;

public class PlaceSearchResponseConverter {
	public static PlaceSearchResponse from(PlaceView placeView, Map<Long, List<Integer>> mapFacilities,
		Map<Long, List<Integer>> reportFacilities) {
		if (placeView.getPlaceType() == PlaceType.map) {
			return new PlaceSearchResponse(placeView.getId(),
				PlaceType.map,
				mapFacilities.get(placeView.getId()),
				placeView.getAddress(),
				placeView.getName());
		} else {
			return new PlaceSearchResponse(placeView.getId(),
				PlaceType.report,
				reportFacilities.get(placeView.getId()),
				placeView.getAddress(),
				placeView.getName());
		}
	}
}
