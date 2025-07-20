package com.example.barrier_free.domain.user.controller;

import com.example.barrier_free.domain.user.dto.OAuthCodeRequest;
import com.example.barrier_free.domain.user.dto.LoginResponse;
import com.example.barrier_free.domain.user.enums.SocialType;
import com.example.barrier_free.domain.user.service.OAuthService;
import com.example.barrier_free.global.response.SuccessCode;
import com.example.barrier_free.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
@Tag(name = "3. 소셜로그인", description = "소셜 로그인 관련 API")
public class OAuthContoller {

    @Value("${kakao.client_id}")
    private String kakao_client_id;

    @Value("${kakao.redirect_uri}")
    private String kakao_redirect_uri;

    @Value("${naver.client_id}")
    private String naver_client_id;

    @Value("${naver.redirect_uri}")
    private String naver_redirect_uri;

    private final OAuthService oAuthService;

    /*
     * 카카오 로그인
     */
    // 1. 카카오 로그인 창으로 이동
    @GetMapping("/kakao")
    @Operation(summary = "카카오 로그인 API",
            description = "카카오 로그인 창으로 이동")
    public void redirectToKakao(HttpServletResponse response) throws IOException {
        String kakaoUrl = "https://kauth.kakao.com/oauth/authorize"
                + "?client_id=" + kakao_client_id
                + "&redirect_uri=" + kakao_redirect_uri
                + "&response_type=code";
        response.sendRedirect(kakaoUrl);
    }

    // 2. 카카오 로그인 성공 후 리다이렉트 URI로 인가코드(code) 발급
    @GetMapping("/kakao-success")
    @Operation(summary = "카카오 로그인 성공 후 인가코드 발급 API",
            description = "로그인 성공 후 인가코드 발급 및 앱으로 리다이렉트")
    public void redirectToAppByKakao(@RequestParam String code,
                              HttpServletResponse response) throws IOException {
        // 앱 전용 딥링크로 리디렉션
        String deepLink = "myapp://login-success?code=" + code;
        response.sendRedirect(deepLink);

        // TODO: 삭제
        System.out.println("카카오 인가 코드: " + code);
    }

    /*
     * 네이버 로그인
     */
    // 1. 네이버 로그인 창으로 이동
    @GetMapping("/naver")
    @Operation(summary = "네이버 로그인 API",
            description = "네이버 로그인 창으로 이동")
    public void redirectToNaver(HttpServletResponse response, HttpSession session) throws IOException {
        // 상태 토큰 생성
        String state = generateState();

        // 상태 토큰 세션에 저장
        // TODO: redis 저장
        session.setAttribute("state", state);

        // 인코딩
        String encodedRedirectUri = URLEncoder.encode(naver_redirect_uri, StandardCharsets.UTF_8);
        String naverUrl = "https://nid.naver.com/oauth2.0/authorize"
                + "?response_type=code"
                + "&client_id=" + naver_client_id
                + "&redirect_uri=" + encodedRedirectUri
                + "&state="+ state;

        response.sendRedirect(naverUrl);
    }

    // 1-1. CSRF 방지를 위한 상태 토큰 생성
    private String generateState() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }

    // 2. 네이버 로그인 성공 후 리다이렉트 URI로 인가코드(code) 발급
    @GetMapping("/naver-success")
    @Operation(summary = "네이버 로그인 성공 후 인가코드 발급 API",
            description = "로그인 성공 후 인가코드 발급 및 앱으로 리다이렉트")
    public void redirectToAppByNaver(@RequestParam String code,
                                     @RequestParam String state,
                                     HttpSession session,
                                     HttpServletResponse response) throws IOException {

        String sessionState = session.getAttribute("state").toString();
        if (!state.equals(sessionState)) {
            throw new RuntimeException("state 값이 일치하지 않습니다!");
        }

        // 앱 전용 딥링크로 리디렉션
        String deepLink = "myapp://login-success?code=" + code + "&state=" + sessionState;
        response.sendRedirect(deepLink);

        // TODO: 삭제
        System.out.println("네이버 인가 코드: " + code);
        System.out.println("네이버 state 코드: " + sessionState);
    }

    /*
    * 공통 로직
    */
    // 3.인가코드로 JWT 발급
    @PostMapping("/token")
    @Operation(summary = "인가코드 > JWT 발급 API",
            description = "앱이 인가코드를 보내면 서버 자체 AccessToken/RefreshToken 발급")
    public ApiResponse<?> exchangeCodeToToken(@RequestBody OAuthCodeRequest oAuthCodeRequest) {
        LoginResponse loginResponse =
                (SocialType.fromString(oAuthCodeRequest.getType()) == SocialType.KAKAO)
                        ? oAuthService.getKakaoAccessToken(oAuthCodeRequest)
                        : oAuthService.getNaverAccessToken(oAuthCodeRequest);
        return ApiResponse.success(SuccessCode.LOGIN_SUCCESSFUL, loginResponse);
    }
}
