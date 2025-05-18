package com.example.barrier_free.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private UserResponse userInfo;
    private String accessToken;
    private String refreshToken;
}
