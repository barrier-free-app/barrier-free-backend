package com.example.barrier_free.domain.place.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.barrier_free.domain.facility.entity.Facility;
import com.example.barrier_free.domain.facility.repository.FacilityRepository;
import com.example.barrier_free.domain.facility.repository.mapFacility.MapFacilityRepository;
import com.example.barrier_free.domain.facility.repository.reportFacility.ReportFacilityRepository;
import com.example.barrier_free.domain.favorite.repository.FavoriteRepository;
import com.example.barrier_free.domain.place.converter.PlaceConverter;
import com.example.barrier_free.domain.place.dto.PlaceDetailResponse;
import com.example.barrier_free.domain.place.dto.PlaceSearchCondition;
import com.example.barrier_free.domain.place.dto.PlaceSearchResponse;
import com.example.barrier_free.domain.place.dto.PlaceSearchResponseConverter;
import com.example.barrier_free.domain.place.dto.PlaceSearchResponsePage;
import com.example.barrier_free.domain.place.dto.PlaceSummaryResponse;
import com.example.barrier_free.domain.place.entity.PlaceView;
import com.example.barrier_free.domain.place.repository.PlaceRepository;
import com.example.barrier_free.domain.user.UserRepository;
import com.example.barrier_free.domain.user.entity.User;
import com.example.barrier_free.global.common.Place;
import com.example.barrier_free.global.common.PlaceFinder;
import com.example.barrier_free.global.common.PlaceType;
import com.example.barrier_free.global.exception.CustomException;
import com.example.barrier_free.global.jwt.JwtUserUtils;
import com.example.barrier_free.global.response.ErrorCode;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PlaceService {

	private final PlaceRepository placeRepository;
	private final MapFacilityRepository mapFacilityRepository;
	private final ReportFacilityRepository reportFacilityRepository;
	private final FacilityRepository facilityRepository;
	private final PlaceFinder placeFinder;
	private final FavoriteRepository favoriteRepository;
	private final UserRepository userRepository;

	public PlaceSearchResponsePage searchPlace(PlaceSearchCondition condition, Pageable pageable) {

		String keyword = condition.getKeyword();
		List<Integer> facilityIds = condition.getFacilities();

		validateFacilityIds(facilityIds);

		Page<PlaceView> placeViewPage = placeRepository.searchPlacesByKeywordAndFacilities(keyword, facilityIds,
			pageable);
		List<PlaceView> placeViews = placeViewPage.getContent();

		List<Long> mapIds = extractIdsByPlaceType(placeViews, PlaceType.map);
		List<Long> reportIds = extractIdsByPlaceType(placeViews, PlaceType.report);

		Map<Long, List<Integer>> mapFacilities = mapFacilityRepository.findFacilitiesByMapIds(mapIds);
		Map<Long, List<Integer>> reportFacilities = reportFacilityRepository.findFacilitiesByReportIds(reportIds);

		List<PlaceSearchResponse> content = placeViewPage.stream()
			.map(p -> PlaceSearchResponseConverter.from(p, mapFacilities, reportFacilities))
			.toList();

		return PlaceSearchResponsePage.of(placeViewPage, content);
	}

	private List<Long> extractIdsByPlaceType(List<PlaceView> views, PlaceType type) {
		return views.stream()
			.filter(p -> p.getPlaceType() == type)
			.map(PlaceView::getId)
			.toList();
	}

	private void validateFacilityIds(List<Integer> facilityIds) {
		if (facilityIds == null || facilityIds.isEmpty())
			return;

		List<Facility> facilities = facilityRepository.findAllByIdIn(facilityIds);
		if (facilities.size() != facilityIds.size()) {
			throw new CustomException(ErrorCode.NOT_FOUND_FACILITY);
		}
	}

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
