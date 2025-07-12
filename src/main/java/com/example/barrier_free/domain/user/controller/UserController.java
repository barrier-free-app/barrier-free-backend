package com.example.barrier_free.domain.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.barrier_free.domain.user.dto.NicknameCheckResponse;
import com.example.barrier_free.domain.user.dto.UpdateNicknameRequest;
import com.example.barrier_free.domain.user.dto.UpdateUserFacilitiesRequest;
import com.example.barrier_free.domain.user.dto.UpdateUserType;
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
		return ApiResponse.success(SuccessCode.OK, "Facilities updated");
	}

	@PutMapping("/nickname")
	public ApiResponse<?> updateNickname(@RequestBody UpdateNicknameRequest request) {
		userService.updateUserNickname(request.getNickname());
		return ApiResponse.success(SuccessCode.OK, "Nickname updated");
	}

	@PutMapping("/userType")
	public ApiResponse<?> updateUserType(@RequestBody UpdateUserType request) {
		userService.updateUserType(request.getUserType());
		return ApiResponse.success(SuccessCode.OK, "UserType updated");
	}

	@GetMapping("/check-nickname")
	public ApiResponse<?> checkNicknameDuplicate(@RequestParam String nickname) {
		NicknameCheckResponse result = userService.checkNicknameDuplicate(nickname);
		return ApiResponse.success(SuccessCode.OK, result);
	}

}
