package com.example.barrier_free.domain.user.controller;

import com.example.barrier_free.domain.user.dto.PasswordRequest;
import com.example.barrier_free.domain.user.service.AuthService;
import com.example.barrier_free.domain.user.service.UserService;
import com.example.barrier_free.global.jwt.JwtUserUtils;
import com.example.barrier_free.global.response.ApiResponse;
import com.example.barrier_free.global.response.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    // 내정보 테스트
    // TODO: 삭제
    @GetMapping("/me")
    public ApiResponse<?> getMe() {
        Long userId = JwtUserUtils.getCurrentUserId();
        return ApiResponse.success(SuccessCode.OK, userService.getUser(userId));
    }

    // 비밀번호 변경
    @PatchMapping("/my-password")
    public ApiResponse<?> updateMyPassword(@RequestBody PasswordRequest passwordRequest ) {
        Long userId = JwtUserUtils.getCurrentUserId();
        return ApiResponse.success(SuccessCode.OK, authService.updatePassword(userId, passwordRequest));
    }
}
