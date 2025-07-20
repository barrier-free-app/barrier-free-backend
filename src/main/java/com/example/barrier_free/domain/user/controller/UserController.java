package com.example.barrier_free.domain.user.controller;

import com.example.barrier_free.domain.user.dto.*;
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

    // 유저 정보 조회
    @GetMapping("/me")
    @Operation(summary = "유저 정보 조회(마이페이지 진입) API",
            description = "로그인 상태 유저의 정보를 조회합니다. ")
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

    // 사용자 편의정보 변경
	@PutMapping("/facilities")
    @Operation(summary = "회원 탈퇴 API",
            description = """
                    로그인 상태 유저의 사용자 편의정보를 변경합니다.
                    
                    - facilityIds: 편의시설 아이디 번호 (예: [1, 2, 4])
                    --- 1: 승강기, 2: 장애인 화장싱, 3: 영유아 동반, 4: 수유실, 5: 경사로
                    """)
	public ApiResponse<?> updateUserFacilities(@RequestBody UpdateUserFacilitiesRequest request) {
		userService.updateUserFacilities(request.getFacilityIds());
		return ApiResponse.success(SuccessCode.OK, "Facilities updated");
	}

    // 사용자 닉네임 변경
	@PutMapping("/nickname")
    @Operation(summary = "닉네임 변경 API",
            description = """
                    로그인 상태 유저의 사용자 편의정보를 변경합니다.
                    
                    - nickname: 변경할 닉네임 (1달 이내 또는 중복된 닉네임은 변경 불가)
                    """)
	public ApiResponse<?> updateNickname(@RequestBody UpdateNicknameRequest request) {

		return ApiResponse.success(SuccessCode.OK, userService.updateUserNickname(request));
	}

    // 사용자 유형 변경
	@PutMapping("/userType")
    @Operation(summary = "사용자 유형 변경 API",
            description = """
                    로그인 상태 유저의 사용자 유형을 변경합니다.
                    
                    - userType: 사용자 유형 (ALL/DISABLED/PREGNANT 중 입력, 소문자 가능)
                    """)
	public ApiResponse<?> updateUserType(@RequestBody UpdateUserType request) {
		userService.updateUserType(request);
		return ApiResponse.success(SuccessCode.OK, "UserType updated");
	}
}
