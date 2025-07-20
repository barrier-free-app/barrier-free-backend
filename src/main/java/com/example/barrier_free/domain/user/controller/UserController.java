package com.example.barrier_free.domain.user.controller;

import com.example.barrier_free.domain.user.dto.DeleteReasonRequest;
import com.example.barrier_free.domain.user.dto.PasswordRequest;
import com.example.barrier_free.domain.user.service.AuthService;
import com.example.barrier_free.domain.user.service.UserService;
import com.example.barrier_free.global.jwt.JwtUserUtils;
import com.example.barrier_free.global.response.ApiResponse;
import com.example.barrier_free.global.response.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "4. 마이페이지", description = "마이페이지 관련 API")
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
    @Operation(summary = "비밀번호 재설정 API",
            description = """
                    로그인 상태 유저의 비밀번호를 재설정합니다.
                    
                    - password: 비밀번호 (영문+숫자 6~16자)
                    - verifyPassword: password와 동일한 비밀번호
                    """)
    public ApiResponse<?> updateMyPassword(@RequestBody PasswordRequest passwordRequest) {
        Long userId = JwtUserUtils.getCurrentUserId();
        return ApiResponse.success(SuccessCode.OK, authService.updatePassword(userId, passwordRequest));
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃 API")
    public ApiResponse<?> logout() {
        Long userId = JwtUserUtils.getCurrentUserId();
        return ApiResponse.success(SuccessCode.OK, authService.logoutUser(userId));
    }

    // 계정 삭제
    @DeleteMapping("/delete")
    @Operation(summary = "회원 탈퇴 API",
            description = """
                    로그인 상태 유저의 계정을 삭제합니다. (바로 삭제됨 주의)
                    
                    - reason: 삭제하려는 이유
                    """)
    public ApiResponse<?> deleteUser(@RequestBody DeleteReasonRequest deleteReasonRequest) {
        Long userId = JwtUserUtils.getCurrentUserId();
        return ApiResponse.success(SuccessCode.OK, userService.deleteUser(userId, deleteReasonRequest));
    }
}
