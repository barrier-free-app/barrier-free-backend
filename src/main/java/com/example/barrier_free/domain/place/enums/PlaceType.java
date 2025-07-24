package com.example.barrier_free.domain.place.enums;

import com.example.barrier_free.domain.favorite.repository.FavoriteRepository;

public enum PlaceType {
	map {
		@Override
		public boolean isFavorite(FavoriteRepository repo, Long userId, Long placeId) {
			return repo.existsByUserIdAndMapId(userId, placeId);
		}
	},
	report {
		@Override
		public boolean isFavorite(FavoriteRepository repo, Long userId, Long placeId) {
			return repo.existsByUserIdAndReportId(userId, placeId);
		}
	};

	public abstract boolean isFavorite(FavoriteRepository repo, Long userId, Long placeId);
}
