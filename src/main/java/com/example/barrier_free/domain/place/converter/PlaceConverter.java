package com.example.barrier_free.domain.place.converter;

import java.util.List;
import java.util.Map;

import com.example.barrier_free.domain.place.dto.PlaceAndFavorite;
import com.example.barrier_free.domain.place.dto.PlaceDetailResponse;
import com.example.barrier_free.domain.place.dto.PlaceMapMarkerResponse;
import com.example.barrier_free.domain.place.dto.PlaceSearchResponse;
import com.example.barrier_free.domain.place.dto.PlaceSummaryResponse;
import com.example.barrier_free.domain.place.entity.PlaceView;
import com.example.barrier_free.domain.place.enums.PlaceType;
import com.example.barrier_free.global.common.Place;

public class PlaceConverter {
	public static PlaceSearchResponse toPlaceSearchResponse(
		PlaceView placeView, Map<Long, List<Integer>> mapFacilities,
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

	public static PlaceMapMarkerResponse toPlaceMapMarkerResponse(Place place) {
		return new PlaceMapMarkerResponse(place.getId(),
			place.getName(),
			place.getLatitude(),
			place.getLongitude(),
			place.getRegion(),
			place.getPlaceType());
	}

	public static PlaceDetailResponse toPlaceDetailResponse(PlaceAndFavorite placeAndFavorite) {
		Place place = placeAndFavorite.place();
		boolean favorite = placeAndFavorite.favorite();

		int reviewCount = place.getReviewCount();
		double avgRating = reviewCount == 0 ? 0.0 :
			Math.round((place.getRatingSum() / reviewCount) * 10.0) / 10.0;

		return new PlaceDetailResponse(
			place.getName(),
			place.getDescription(),
			place.getAddress(),
			place.getOpeningHours(),
			place.getFacility(),
			place.getPlaceType(),
			place.getImageType().getCode(),
			favorite,
			reviewCount,
			avgRating

		);
	}

	public static PlaceSummaryResponse toPlaceSummaryResponse(PlaceAndFavorite placeAndFavorite) {
		Place place = placeAndFavorite.place();
		boolean favorite = placeAndFavorite.favorite();
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
