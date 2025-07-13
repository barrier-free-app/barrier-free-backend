package com.example.barrier_free.domain.user.controller;

import com.example.barrier_free.domain.user.dto.LoginRequest;
import com.example.barrier_free.domain.user.dto.SignupRequest;
import com.example.barrier_free.domain.user.service.AuthService;
import com.example.barrier_free.global.response.ApiResponse;
import com.example.barrier_free.global.response.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "2. 계정", description = "계정 관련 API")
public class AuthController {

    private final AuthService authService;

    // 회원가입
    @PostMapping("/signup")
    @Operation(summary = "회원가입 API",
            description = """
                    이메일 인증코드 확인 후 추가정보 입력으로 회원가입을 진행합니다. (이메일 인증코드 확인 API 우선)
                    
                    - email: 인증 완료된 이메일 주소
                    - nickname: 닉네임
                    - username: 아이디 (영문+숫자 6~16자)
                    - password: 비밀번호 (영문+숫자 6~16자)
                    - verifyPassword: password와 동일한 비밀번호
                    - userType: 사용자 유형 (ALL/DISABLED/PREGNANT 중 입력, 소문자 가능)
                    - userFacilityIds: 편의시설 아이디 번호 (예: [1, 2, 4])
                    --- 1: 승강기, 2: 장애인 화장싱, 3: 영유아 동반, 4: 수유실, 5: 경사로
                    """)
    public ApiResponse<?> signup(@RequestBody SignupRequest signupRequest) {
        return ApiResponse.success(SuccessCode.USER_CREATED, authService.signupUser(signupRequest));
    }

    // 회원가입 - 아이디 중복 확인
    @GetMapping("/signup/verify")
    @Operation(summary = "아이디 중복 확인 API",
            description = """
                    회원가입 진행 시 아이디를 중복 확인합니다.
                    
                    
                    - type: 확인할 타입 (nickname/username 중 입력)
                    - input: 확인할 값
                    """)
    public ApiResponse<?> verifyInputDuplicate(@RequestParam String type, @RequestParam String input) {
        return ApiResponse.success(SuccessCode.OK, authService.verifyInputDuplicate(type, input));
    }

    // 일반 로그인
    @PostMapping("/login")
    @Operation(summary = "일반 로그인 API",
            description = """
                    일반 로그인을 진행합니다. \n
                    
                    - username: 이메일 또는 아이디 (영문+숫자 6~16자, 테스트 값 - nimuyman@gmail.com/test123)
                    - password: 비밀번호 (영문+숫자 6~16자, 테스트 값 - a12345)
                    """)
    public ApiResponse<?> signup(@RequestBody LoginRequest loginRequest) {
        return ApiResponse.success(SuccessCode.OK, authService.loginUser(loginRequest));
    }


}
