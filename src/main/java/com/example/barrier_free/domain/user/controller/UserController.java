package com.example.barrier_free.domain.user.controller;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.barrier_free.domain.user.dto.UpdateUserFacilitiesRequest;
import com.example.barrier_free.domain.user.service.UserService;
import com.example.barrier_free.global.response.ApiResponse;
import com.example.barrier_free.global.response.SuccessCode;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
	private final UserService userService;

	@PutMapping("/facilities")
	public ApiResponse<?> updateUserFacilities(@RequestBody UpdateUserFacilitiesRequest request) {
		userService.updateUserFacilities(request.getFacilityIds());
		return ApiResponse.success(SuccessCode.OK, "update success");
	}

}
