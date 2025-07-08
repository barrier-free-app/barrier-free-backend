package com.example.barrier_free.domain.user.controller;

import com.example.barrier_free.domain.user.dto.KakaoAuthCodeRequest;
import com.example.barrier_free.domain.user.dto.LoginResponse;
import com.example.barrier_free.domain.user.service.KakaoLoginService;
import com.example.barrier_free.global.response.SuccessCode;
import com.example.barrier_free.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class OAuthContoller {

    @Value("${kakao.client_id}")
    private String client_id;

    @Value("${kakao.redirect_uri}")
    private String redirect_uri;

    private final KakaoLoginService kakaoLoginService;

    // 1. 카카오 로그인 창으로 이동
    @GetMapping("/kakao")
    @Operation(summary = "카카오 로그인 API",
            description = "유저 정보, Access Token, Refresh Token 응답")
    public void redirectToKakao(HttpServletResponse response) throws IOException {
        String kakaoUrl = "https://kauth.kakao.com/oauth/authorize"
                + "?client_id=" + client_id
                + "&redirect_uri=" + redirect_uri
                + "&response_type=code";
        response.sendRedirect(kakaoUrl);
    }

    // 2. 카카오 로그인 성공 후 리다이렉트 URI로 인가코드(code) 발급
    @GetMapping("/kakao/success")
    @Operation(summary = "카카오 로그인 성공 후 인가코드 발급 API",
            description = "유저 정보, Access Token, Refresh Token 응답")
    public void redirectToApp(@RequestParam String code, HttpServletResponse response) throws IOException {
        // 앱 전용 딥링크로 리디렉션
        String deepLink = "myapp://login-success?code=" + code;
        response.sendRedirect(deepLink);

        System.out.println("인가 코드: " + code);
    }

    // 3.인가코드로 JWT 발급
    @PostMapping("/token")
    @Operation(summary = "인가코드로 JWT 발급",
            description = "앱이 인가코드를 보내면 서버 자체 AccessToken/RefreshToken 발급")
    public ApiResponse<?> exchangeCodeToToken(@RequestBody KakaoAuthCodeRequest code) {
        LoginResponse loginResponse = kakaoLoginService.getKakaoAccessToken(code);
        return ApiResponse.success(SuccessCode.LOGIN_SUCCESSFUL, loginResponse);
    }
}
