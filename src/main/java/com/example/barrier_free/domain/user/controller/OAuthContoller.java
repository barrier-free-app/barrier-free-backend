package com.example.barrier_free.domain.user.controller;

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
@RequestMapping("/auth/oauth")
@RequiredArgsConstructor
public class OAuthContoller {

    @Value("${kakao.client_id}")
    private String client_id;

    @Value("${kakao.redirect_uri}")
    private String redirect_uri;

    private final KakaoLoginService kakaoLoginService;

    // 1. 카카오 로그인 창으로 이동
    // 로그인 성공 후 리다이렉트 URI로 인가 코드 보내줌
    @GetMapping("/")
    public void redirectToKakao(HttpServletResponse response) throws IOException {
        String kakaoUrl = "https://kauth.kakao.com/oauth/authorize"
                + "?client_id=" + client_id
                + "&redirect_uri=" + redirect_uri
                + "&response_type=code";
        response.sendRedirect(kakaoUrl);
    }

    // 2. 인가코드 받아 로그인 진행
    @GetMapping("/kakao")
    @Operation(summary = "카카오 로그인 API",
            description = "유저 정보, Access Token, Refresh Token 응답")
    public ApiResponse<?> loginWithKakao(@RequestParam String code) {
        LoginResponse loginResponse =  kakaoLoginService.getKakaoAccessToken(code);
        return ApiResponse.success(SuccessCode.LOGIN_SUCCESSFUL,loginResponse);
    }
}
