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

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class FavoriteController {

	private final FavoriteService favoriteService;

	/******테스트용임*****/
	@GetMapping("/favorite/rank/test")
	public void testRankSave() {
		favoriteService.saveMonthlyRanking();
	}

	/******테스트용임******/

	@PostMapping("/favorites")
	public ApiResponse<Boolean> toggleFavorite(@RequestBody FavoriteRequestDto request) {
		boolean result = favoriteService.toggleFavorite(request);
		return ApiResponse.success(SuccessCode.OK, result); // true면 등록, false면 취소
	}

	@GetMapping("/places/populars")
	public ApiResponse<?> getMonthlyTop3() {
		List<PlaceResponse> monthlyTop3 = favoriteService.getMonthlyTop3();
		return ApiResponse.success(SuccessCode.OK, monthlyTop3);
	}

}