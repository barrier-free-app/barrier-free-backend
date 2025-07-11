package com.example.barrier_free.domain.place.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.barrier_free.domain.place.dto.PlaceSearchCondition;
import com.example.barrier_free.domain.place.dto.PlaceSearchResponsePage;
import com.example.barrier_free.domain.place.service.PlaceService;
import com.example.barrier_free.global.response.ApiResponse;
import com.example.barrier_free.global.response.SuccessCode;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PlaceController {

	private final PlaceService placeService;

	@GetMapping(value = "/places/search")
	public ApiResponse<?> searchPlace(
		PlaceSearchCondition condition,
		@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
		PlaceSearchResponsePage placeSearchResponsePage = placeService.searchPlace(condition, pageable);
		return ApiResponse.success(SuccessCode.OK, placeSearchResponsePage);
	}
}
