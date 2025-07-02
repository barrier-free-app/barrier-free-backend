package com.example.barrier_free.domain.favorite.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.barrier_free.domain.favorite.FavoriteService;
import com.example.barrier_free.domain.favorite.dto.FavoriteRequestDto;
import com.example.barrier_free.domain.favorite.dto.PlaceResponse;
import com.example.barrier_free.global.response.ApiResponse;
import com.example.barrier_free.global.response.SuccessCode;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class FavoriteController {

	private final FavoriteService favoriteService;

	@Operation(
		summary = "주간 랭킹 저장(테스트용)",
		description =
			"Redis에 저장된 *이번 주*(지난주 아님!) 인기 장소 데이터를 기반으로 주간랭킹을 수동 저장합니다. 주차가 지나지 않았을 경우 테스트용 혹은 연동시에 사용하는용도입니다!"
				+ "한 번만 요청하시면 db에 이번주 인기 장소가 들어갑니다!!!"
				+ "실제로는 자동 스케줄러가 지난 주 좋아요 수를 이용해 처리합니다!"
	)
	/******테스트용임*****/
	@GetMapping("/favorite/rank/test")
	public void testRankThisWeekSave() {
		favoriteService.saveCurrentWeeklyRanking();
	}

	/******테스트용임******/

	@PostMapping("/favorites")
	public ApiResponse<Boolean> toggleFavorite(@RequestBody FavoriteRequestDto request) {
		boolean result = favoriteService.toggleFavorite(request);
		return ApiResponse.success(SuccessCode.OK, result); // true면 등록, false면 취소
	}

	@GetMapping("/places/populars")
	public ApiResponse<?> getWeeklyTop3() {
		List<PlaceResponse> weeklyTop3 = favoriteService.getWeeklyTop3();
		return ApiResponse.success(SuccessCode.OK, weeklyTop3);
	}

}