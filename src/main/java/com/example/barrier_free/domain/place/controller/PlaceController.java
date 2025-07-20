package com.example.barrier_free.domain.place.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.barrier_free.domain.place.dto.PlaceDetailResponse;
import com.example.barrier_free.domain.place.dto.PlaceMapMarkerResponse;
import com.example.barrier_free.domain.place.dto.PlaceSearchCondition;
import com.example.barrier_free.domain.place.dto.PlaceSearchResponsePage;
import com.example.barrier_free.domain.place.dto.PlaceSummaryResponse;
import com.example.barrier_free.domain.place.service.PlaceService;
import com.example.barrier_free.global.common.PlaceType;
import com.example.barrier_free.global.response.ApiResponse;
import com.example.barrier_free.global.response.SuccessCode;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@Tag(name = "장소", description = "장소 검색 및 장소 관련 API")
@RequestMapping("/places")
public class PlaceController {
	private final PlaceService placeService;

	@GetMapping(value = "/search")
	public ApiResponse<?> searchPlace(
		PlaceSearchCondition condition,
		@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
		PlaceSearchResponsePage placeSearchResponsePage = placeService.searchPlace(condition, pageable);
		return ApiResponse.success(SuccessCode.OK, placeSearchResponsePage);
	}

	@GetMapping("/{placeId}/summary")
	public ApiResponse<?> getSummaryOfPlace(@PathVariable Long placeId,
		@RequestParam(required = true) PlaceType placeType
	) {
		PlaceSummaryResponse placeSummaryResponse = placeService.getSummary(placeId, placeType);
		return ApiResponse.success(SuccessCode.OK, placeSummaryResponse);
	}

	@GetMapping("/{placeId}/detail")
	public ApiResponse<?> getDetailOfPlace(@PathVariable Long placeId,
		@RequestParam(required = true) PlaceType placeType
	) {
		PlaceDetailResponse placeDetailResponse = placeService.getDetail(placeId, placeType);
		return ApiResponse.success(SuccessCode.OK, placeDetailResponse);
	}

	@GetMapping("/places/all")
	public ApiResponse<?> getAllPlacesForMap() {
		List<PlaceMapMarkerResponse> markers = placeService.getAllPlaceMarkers();
		return ApiResponse.success(SuccessCode.OK, markers);
	}
}
