package com.example.barrier_free.domain.favorite.repository;

import java.util.List;

import com.example.barrier_free.domain.favorite.dto.FavoritePlaceGroup;

public interface FavoriteRepositoryCustom {
	FavoritePlaceGroup findFilteredFavorites(Long userId, List<Integer> facilityIds);
}
