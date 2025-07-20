package com.example.barrier_free.global.jwt;

import com.example.barrier_free.domain.user.UserRepository;
import com.example.barrier_free.domain.user.entity.User;
import com.example.barrier_free.global.exception.CustomException;
import com.example.barrier_free.global.response.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

import static com.example.barrier_free.global.response.ErrorCode.USER_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class JwtTokenUtils {

    private final SecretKey key = Jwts.SIG.HS256.key().build();

    private static final Long ACCESS_TOKEN_EXPIRATION =  60 * 60 * 1000L; // 1시간
    private static final Long REFRESH_TOKEN_EXPIRATION = 7 * 24 * 60 * 60 * 1000L;  // 7일

    private final UserRepository userRepository;

    public String createAccessToken(Long id) {
        return Jwts.builder()
                .subject(String.valueOf(id))
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + ACCESS_TOKEN_EXPIRATION))
                .signWith(key)
                .compact();
    }

    public String createRefreshToken(Long id) {
        return Jwts.builder()
                .subject(String.valueOf(id))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .signWith(key)
                .compact();
    }

    public Long validateToken(String token) {
        Claims claims;

        // jwt 토큰 파싱
        try{
            claims = Jwts
                    .parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        }
        catch(ExpiredJwtException e) {
            throw new CustomException(ErrorCode.JWT_EXPIRED);
        }
        catch (JwtException e) {
            throw new CustomException(ErrorCode.JWT_EXPIRED);
        }
        catch (Exception e) {
            throw new CustomException(ErrorCode.JWT_UNKNOWN_ERROR);
        }

        // 파싱 이후
        Long userId = Long.parseLong(claims.getSubject());
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        if (user.getAccessToken() == null || !user.getAccessToken().equals(token)) {
            throw new CustomException(ErrorCode.JWT_REVOKED);  // 무효화된 토큰
        }

        return userId;
    }
}
