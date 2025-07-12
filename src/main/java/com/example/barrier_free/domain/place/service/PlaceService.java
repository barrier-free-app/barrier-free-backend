package com.example.barrier_free.domain.place.service;

import org.springframework.stereotype.Service;

import com.example.barrier_free.domain.favorite.repository.FavoriteRepository;
import com.example.barrier_free.domain.map.repository.MapRepository;
import com.example.barrier_free.domain.place.converter.PlaceConverter;
import com.example.barrier_free.domain.place.dto.PlaceDetailResponse;
import com.example.barrier_free.domain.place.dto.PlaceSummaryResponse;
import com.example.barrier_free.domain.report.repository.ReportRepository;
import com.example.barrier_free.domain.user.UserRepository;
import com.example.barrier_free.domain.user.entity.User;
import com.example.barrier_free.global.common.Place;
import com.example.barrier_free.global.common.PlaceFinder;
import com.example.barrier_free.global.common.PlaceType;
import com.example.barrier_free.global.exception.CustomException;
import com.example.barrier_free.global.jwt.JwtUserUtils;
import com.example.barrier_free.global.response.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlaceService {
	private final ReportRepository reportRepository;
	private final MapRepository mapRepository;
	private final PlaceFinder placeFinder;
	private final FavoriteRepository favoriteRepository;
	private final UserRepository userRepository;

	public PlaceSummaryResponse getSummary(Long placeId, PlaceType placeType) {
		Place place = placeFinder.findPlace(placeId, placeType);
		Long currentUserId = JwtUserUtils.getCurrentUserId();
		User user = userRepository.findById(currentUserId)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

		boolean favoriteStatus = isFavorite(user.getId(), placeId, placeType);
		return PlaceConverter.toPlaceSummaryResponse(place, favoriteStatus);

	}

	public PlaceDetailResponse getDetail(Long placeId, PlaceType placeType) {
		Place place = placeFinder.findPlace(placeId, placeType);
		Long currentUserId = JwtUserUtils.getCurrentUserId();
		User user = userRepository.findById(currentUserId)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

		boolean favoriteStatus = isFavorite(user.getId(), placeId, placeType);

		return PlaceConverter.toPlaceDetailResponse(place, favoriteStatus);

	}

	private boolean isFavorite(Long userId, Long placeId, PlaceType placeType) {
		if (placeType == PlaceType.map) {
			return favoriteRepository.existsByUserIdAndMapId(userId, placeId);
		} else {
			return favoriteRepository.existsByUserIdAndReportId(userId, placeId);
		}

	}

}
