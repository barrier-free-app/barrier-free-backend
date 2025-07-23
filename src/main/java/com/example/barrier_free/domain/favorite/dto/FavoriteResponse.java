package com.example.barrier_free.domain.favorite.dto;

import java.util.List;

import com.example.barrier_free.global.common.Place;
import com.example.barrier_free.global.common.PlaceType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FavoriteResponse {
	private Long placeId;
	private PlaceType placeType;
	private String name;
	private String description;
	private List<Integer> facility;
	private int imageType;
	private boolean favorite;

	public static FavoriteResponse fromPlace(Place place) {
		return new FavoriteResponse(
			place.getId(),
			place.getPlaceType(),
			place.getName(),
			place.getDescription(),
			place.getFacility(),
			place.getImageType().getCode(),
			true
		);
	}
}
