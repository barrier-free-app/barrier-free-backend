package com.example.barrier_free.global.auth;

import com.example.barrier_free.global.exception.SuccessCode;
import com.example.barrier_free.global.response.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/redirect")
public class OAuthLoginContoller {

    @Value("${kakao.client_id}")
    private String client_id;

    @Value("${kakao.redirect_uri}")
    private String redirect_uri;

    // 1. 카카오 로그인창 시작
    @GetMapping
    public void redirectToKakao(HttpServletResponse response) throws IOException {
        String kakaoUrl = "https://kauth.kakao.com/oauth/authorize"
                + "?client_id=" + client_id
                + "&redirect_uri=" + redirect_uri
                + "&response_type=code";
        response.sendRedirect(kakaoUrl);
    }

    // 2. 카카오가 로그인 성공 후 인가 코드 보내주는 URI
    @GetMapping("/callback")
    public ApiResponse<String> kakaoCallback(@RequestParam String code) {
        // 여기서 code 받아서 access token 요청 및 사용자 정보 요청
        return ApiResponse.success(SuccessCode.OK, code);
    }
}
