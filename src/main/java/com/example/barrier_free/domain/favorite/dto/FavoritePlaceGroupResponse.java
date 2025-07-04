package com.example.barrier_free.domain.favorite.dto;

import java.util.List;

import lombok.Getter;

@Getter
public class FavoritePlaceGroupResponse {
	private List<PlaceResponse> mapFavorites;
	private List<PlaceResponse> reportFavorites;

	public FavoritePlaceGroupResponse(List<PlaceResponse> mapFavorites,
		List<PlaceResponse> reportFavorites) {
		this.mapFavorites = mapFavorites;
		this.reportFavorites = reportFavorites;
	}
}
