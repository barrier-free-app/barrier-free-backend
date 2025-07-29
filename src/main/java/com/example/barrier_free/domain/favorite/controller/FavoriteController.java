package com.example.barrier_free.domain.favorite.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.barrier_free.domain.favorite.FavoriteService;
import com.example.barrier_free.domain.favorite.dto.FavoritePlaceGroupResponse;
import com.example.barrier_free.domain.favorite.dto.FavoriteRequestDto;
import com.example.barrier_free.domain.favorite.dto.PlaceRankResponse;
import com.example.barrier_free.global.response.ApiResponse;
import com.example.barrier_free.global.response.SuccessCode;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "좋아요/주간 랭킹", description = "좋아요 및 인기 장소 관련 API")
public class FavoriteController {

	private final FavoriteService favoriteService;

	@Operation(
		summary = "좋아요 등록/해제 API",
		description = """
			사용자가 장소에 좋아요(즐겨찾기)를 등록하거나 해제합니다.  
			이미 등록되어 있으면 즐겨찾기를 해제하고, 없으면 새로 등록합니다.  
			- 응답값: true = 등록됨, false = 해제됨
			"""
	)

	@PostMapping("/favorites")
	public ApiResponse<Boolean> toggleFavorite(@RequestBody @Valid FavoriteRequestDto request) {
		boolean result = favoriteService.toggleFavorite(request);
		return ApiResponse.success(SuccessCode.OK, result); // true면 등록, false면 취소
	}

	@Operation(
		summary = "주간 인기 장소 Top 3 조회 API",
		description = """
			- 좋아요 순위 Top 3 장소 목록을 반환합니다.  
			"""
	)
	@GetMapping("/places/populars")
	public ApiResponse<?> getWeeklyTop3() {
		List<PlaceRankResponse> weeklyTop3 = favoriteService.getWeeklyTop3();
		return ApiResponse.success(SuccessCode.OK, weeklyTop3);
	}

	@Operation(
		summary = "사용자 좋아요 목록 조회 API",
		description = """
			- 현재 로그인한 사용자가 좋아요한 장소 목록을 조회합니다.  
			- 특정 편의시설이 있는 장소만 필터링하고 싶다면 `facilities` 파라미터를 사용하세요.  
			- 필터링 없이 전체 즐겨찾기를 조회하려면 파라미터 없이 호출하면 됩니다.
			"""
	)
	@GetMapping(value = "/users/favorites")
	public ApiResponse<?> getFavorite(
		@RequestParam(required = false) List<Integer> facilities
	) {
		FavoritePlaceGroupResponse response = favoriteService.getFavorite(facilities);
		return ApiResponse.success(SuccessCode.OK, response);

	}

}