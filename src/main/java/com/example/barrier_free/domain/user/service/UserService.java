package com.example.barrier_free.domain.user.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.barrier_free.domain.favorite.dto.FavoritePlaceGroup;
import com.example.barrier_free.domain.favorite.dto.FavoritePlaceGroupResponse;
import com.example.barrier_free.domain.favorite.dto.PlaceResponse;
import com.example.barrier_free.domain.favorite.repository.FavoriteRepository;
import com.example.barrier_free.domain.user.UserRepository;
import com.example.barrier_free.domain.user.entity.User;
import com.example.barrier_free.global.exception.CustomException;
import com.example.barrier_free.global.response.ErrorCode;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class UserService {
	private final UserRepository userRepository;
	private final FavoriteRepository favoriteRepository;

	@Transactional
	public FavoritePlaceGroupResponse getFavorite(Long userId, List<Integer> facilities) {

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

		//1. 필터없는 경우  2. 필터 존재하는 경우
		FavoritePlaceGroup favoritePlace = isNoFilter(facilities)
			? getAllFavorites(userId)
			: getFilteredFavorites(userId, facilities);

		List<PlaceResponse> mapResponses = favoritePlace.getMapFavorites().stream()
			.map(PlaceResponse::fromPlace)
			.toList();

		List<PlaceResponse> reportResponses = favoritePlace.getReportFavorites().stream()
			.map(PlaceResponse::fromPlace)
			.toList();

		return new FavoritePlaceGroupResponse(mapResponses, reportResponses);
	}

	private FavoritePlaceGroup getFilteredFavorites(Long userId, List<Integer> facilities) {
		return favoriteRepository.findFilteredFavorites(userId, facilities);
	}

	private FavoritePlaceGroup getAllFavorites(Long userId) {
		return favoriteRepository.findFavoriteByUserIdWithPlace(userId);
	}

	private boolean isNoFilter(List<Integer> facilities) {
		return facilities == null || facilities.isEmpty();
	}
}
