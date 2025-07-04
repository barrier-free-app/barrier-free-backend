package com.example.barrier_free.global.jwt;

import com.example.barrier_free.global.exception.CustomException;
import com.example.barrier_free.global.response.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtManager {

    private final JwtTokenUtils jwtTokenUtils;

    // accessToken 발급
    public String createAccessToken(Long userId) {
        return jwtTokenUtils.createAccessToken(userId);
    }

    // refreshToken 발급
    public String createRefreshToken(Long userId) {
        return jwtTokenUtils.createRefreshToken(userId);
    }

    // accessToken 검증
    public Long validateAccessToken(String token) {
        return jwtTokenUtils.validateToken(token);
    }

    // refreshToken 검증
    public Long validateRefreshToken(String token) {
        return jwtTokenUtils.validateToken(token);
    }

    // Authorization 헤더에서 토큰 추출
    public String getToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        throw new CustomException(ErrorCode.JWT_NOT_FOUND);
    }
}
