package com.example.barrier_free.domain.user.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.barrier_free.domain.favorite.dto.FavoritePlaceGroupResponse;
import com.example.barrier_free.domain.user.service.UserService;
import com.example.barrier_free.global.response.ApiResponse;
import com.example.barrier_free.global.response.SuccessCode;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
public class UserController {

	private final UserService userService;

	@GetMapping(value = "/users/{userId}/favorites")
	public ApiResponse<?> getFavorite(@PathVariable Long userId,
		@RequestParam(required = false) List<Integer> facilities
	) {
		FavoritePlaceGroupResponse response = userService.getFavorite(userId, facilities);
		return ApiResponse.success(SuccessCode.OK, response);

	}
}
