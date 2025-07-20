package com.example.barrier_free.domain.user.service;

import com.example.barrier_free.domain.user.UserRepository;
import com.example.barrier_free.domain.user.dto.OAuthCodeRequest;
import com.example.barrier_free.domain.user.dto.LoginResponse;
import com.example.barrier_free.domain.user.entity.User;
import com.example.barrier_free.domain.user.enums.SocialType;
import com.example.barrier_free.global.jwt.JwtManager;
import com.fasterxml.jackson.databind.JsonNode;
import io.netty.handler.codec.http.HttpHeaderValues;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class OAuthService {

    @Value("${kakao.client_id}")
    private String kakao_client_id;

    @Value("${kakao.redirect_uri}")
    private String kakao_redirect_uri;

    @Value("${naver.client_id}")
    private String naver_client_id;

    @Value("${naver.client_secret}")
    private String naver_client_secret;

    private final UserRepository userRepository;
    private final JwtManager jwtManager;

    // 인가코드로부터 카카오 액세스 토큰 발급
    public LoginResponse getKakaoAccessToken(OAuthCodeRequest oAuthCodeRequest) {

        // 카카오 로그인 api 이용하므로 WebClient 이용
        JsonNode tokenNode = WebClient.create("https://kauth.kakao.com")
                .post()
                .uri("/oauth/token")
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                        .with("client_id", kakao_client_id)
                        .with("redirect_uri", kakao_redirect_uri)
                        .with("code", oAuthCodeRequest.getAuthCode()))
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        String kAccessToken = tokenNode.get("access_token").asText();

        // 카카오 액세스 토큰으로부터 유저정보 추출
        JsonNode userInfoNode = getKakaoUserInfo(kAccessToken);

        String email = userInfoNode.get("kakao_account").get("email").asText();
        String nickname = userInfoNode.get("properties").get("nickname").asText();

        // 우리 db에 유저 없으면 유저 생성
        return getOrCreateUser(email, nickname, SocialType.KAKAO);
    }


    // 인가코드로부터 카카오 액세스 토큰 발급
    public LoginResponse getNaverAccessToken(OAuthCodeRequest oAuthCodeRequest) {

        // 네이버 로그인 api 이용하므로 WebClient 이용
        JsonNode tokenNode = WebClient.create("https://nid.naver.com")
                .post()
                .uri("oauth2.0/token")
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                        .with("client_id", naver_client_id)
                        .with("client_secret", naver_client_secret)
                        .with("redirect_uri", kakao_redirect_uri)
                        .with("code", oAuthCodeRequest.getAuthCode())
                        .with("state", oAuthCodeRequest.getState()))
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        String nAccessToken = tokenNode.get("access_token").asText();

        // 네이버 액세스 토큰으로부터 유저정보 추출
        JsonNode userInfoNode = getNaverUserInfo(nAccessToken);

        String email = userInfoNode.get("response").get("email").asText();
        String nickname = userInfoNode.get("response").get("nickname").asText();

        // 우리 db에 유저 없으면 유저 생성
        return getOrCreateUser(email, nickname, SocialType.NAVER);
    }

    // 유저 존재 확인
    private LoginResponse getOrCreateUser(String email, String nickname, SocialType socialType) {

        // 유저 조회
        User user = userRepository.findByEmail(email);

        // 없으면 생성
        if (user == null) {
            user = userRepository.save(new User(email, nickname, socialType));
        }

        // 있으면 정보 조회 및 토큰 발급
        Long userId = user.getId();
        String accessToken = jwtManager.createAccessToken(userId);
        String refreshToken = jwtManager.createRefreshToken(userId);

        user.setTokens(accessToken, refreshToken);
        userRepository.save(user);

        return new LoginResponse(userId, accessToken, refreshToken);
    }

    // 카카오 토큰으로부터 유저 정보 가져오기
    public JsonNode getKakaoUserInfo(String kAccessToken) {

        return WebClient.create("https://kapi.kakao.com")
                .post()
                .uri("/v2/user/me")
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + kAccessToken)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
    }

    // 네이버 토큰으로부터 유저 정보 가져오기
    public JsonNode getNaverUserInfo(String nAccessToken) {

        return WebClient.create("https://openapi.naver.com")
                .get()
                .uri("/v1/nid/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + nAccessToken)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
    }

}
