package com.example.barrier_free.domain.favorite.dto;

import java.util.List;

import lombok.Getter;

@Getter
public class FavoritePlaceGroupResponse {
	private List<FavoriteResponse> mapFavorites;
	private List<FavoriteResponse> reportFavorites;

	public FavoritePlaceGroupResponse(List<FavoriteResponse> mapFavorites,
		List<FavoriteResponse> reportFavorites) {
		this.mapFavorites = mapFavorites;
		this.reportFavorites = reportFavorites;
	}
}
