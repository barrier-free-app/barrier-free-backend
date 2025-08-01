package com.example.barrier_free.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OAuthCodeRequest {
    private String type;
    private String authCode;
    private String state;
}
