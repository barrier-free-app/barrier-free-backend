package com.example.barrier_free.domain.recommendation.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.barrier_free.domain.place.enums.PlaceType;
import com.example.barrier_free.domain.recommendation.dto.RecommendationPlaceResponse;
import com.example.barrier_free.domain.recommendation.enums.RecommendationType;
import com.example.barrier_free.global.response.ApiResponse;
import com.example.barrier_free.global.response.SuccessCode;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/recommendations")
@RequiredArgsConstructor
@Tag(name = "추천", description = "추천 장소 조회 API(더미데이터-연동용)")
public class RecommendationController {

	@GetMapping
	public ApiResponse<List<RecommendationPlaceResponse>> getRecommendations(
		@RequestParam RecommendationType type,
		@RequestParam(required = false) List<Integer> facilities
	) {
		// TODO: 실제 추천 로직으로 대체

		// 더미 응답
		List<RecommendationPlaceResponse> dummy = List.of(
			new RecommendationPlaceResponse(
				1L,
				PlaceType.map,
				"햇살 좋은 공원",
				"마포구",
				"산책하기 좋은 공원",
				List.of(1, 3),
				2
			),
			new RecommendationPlaceResponse(
				2L,
				PlaceType.report,
				"편한 카페",
				"종로구",
				"휠체어 접근이 편한 장소입니다.",
				List.of(2, 5), // 장애인 화장실, 경사로
				1
			),
			new RecommendationPlaceResponse(
				3L,
				PlaceType.report,
				"눈송이네 피자",
				"용산구",
				"모두와 함께하는 눈송이!",
				List.of(2, 3), // 장애인 화장실, 경사로
				1)
		);

		return ApiResponse.success(SuccessCode.OK, dummy);

	}
}
