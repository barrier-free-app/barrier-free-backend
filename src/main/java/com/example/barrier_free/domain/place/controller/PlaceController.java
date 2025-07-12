package com.example.barrier_free.domain.place.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.barrier_free.domain.place.service.PlaceService;
import com.example.barrier_free.global.common.PlaceType;
import com.example.barrier_free.global.response.ApiResponse;
import com.example.barrier_free.global.response.SuccessCode;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class PlaceController {
	private final PlaceService placeService;

	@GetMapping(value = "/places/search")
	public ApiResponse<?> searchPlace(
		PlaceSearchCondition condition,
		@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
		PlaceSearchResponsePage placeSearchResponsePage = placeService.searchPlace(condition, pageable);
		return ApiResponse.success(SuccessCode.OK, placeSearchResponsePage);
	@GetMapping("/places/{placeId}/summary")
	public ApiResponse<?> getSummaryOfPlace(@PathVariable Long placeId,
		@RequestParam(required = true) PlaceType placeType
	) {
		PlaceSummaryResponse placeSummaryResponse = placeService.getSummary(placeId, placeType);
		return ApiResponse.success(SuccessCode.OK, placeSummaryResponse);
	}

	@GetMapping("/places/{placeId}/detail")
	public ApiResponse<?> getDetailOfPlace(@PathVariable Long placeId,
		@RequestParam(required = true) PlaceType placeType
	) {
		PlaceDetailResponse placeDetailResponse = placeService.getDetail(placeId, placeType);
		return ApiResponse.success(SuccessCode.OK, placeDetailResponse);
	}

}
