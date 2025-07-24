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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@Tag(name = "장소", description = "장소 검색 및 장소 조회 API")
@RequestMapping("/places")
public class PlaceController {
	private final PlaceService placeService;

	@Operation(
		summary = "장소 검색 API",
		description = """
			검색어&편의시설로 장소 필터링이 이루어 집니다.
			페이징 형식입니다.
			기본 정렬은 생성일 내림차순
			- page (기본값: 0): 페이지 번호 (0부터 시작)
			- size (기본값: 10): 한 페이지당 항목 수
			- sort 정렬 기준			
			- keyword 검색어
			- facilities 편의시설
			응답값
			- hasNext: 다음 페이지 존재여부
			- totalPages: 전체 페이지 수
			"""
	)
	@GetMapping("/search")
	public ApiResponse<?> searchPlace(
		PlaceSearchCondition condition,
		@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
		PlaceSearchResponsePage placeSearchResponsePage = placeService.searchPlace(condition, pageable);
		return ApiResponse.success(SuccessCode.OK, placeSearchResponsePage);
	}

	@Operation(
		summary = "장소 요약 정보 조회 API",
		description = """
			마커 클릭시 뜨는 요약 정보 조회를 위한 API 입니다.
			"""
	)
	@GetMapping("/{placeId}/summary")
	public ApiResponse<?> getSummaryOfPlace(@PathVariable Long placeId,
		@RequestParam(required = true) PlaceType placeType
	) {
		PlaceSummaryResponse placeSummaryResponse = placeService.getSummary(placeId, placeType);
		return ApiResponse.success(SuccessCode.OK, placeSummaryResponse);
	}

	@Operation(
		summary = "장소 세부 정보 조회 API"
	)
	@GetMapping("/{placeId}/detail")
	public ApiResponse<?> getDetailOfPlace(@PathVariable Long placeId,
		@RequestParam(required = true) PlaceType placeType
	) {
		PlaceDetailResponse placeDetailResponse = placeService.getDetail(placeId, placeType);
		return ApiResponse.success(SuccessCode.OK, placeDetailResponse);
	}

	@Operation(
		summary = "지도에서 장소 전체 조회 API"
	)
	@GetMapping("/all")
	public ApiResponse<?> getAllPlacesForMap() {
		List<PlaceMapMarkerResponse> markers = placeService.getAllPlaceMarkers();
		return ApiResponse.success(SuccessCode.OK, markers);
	}
}
