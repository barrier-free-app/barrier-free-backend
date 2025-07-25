package com.example.barrier_free.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class SignupRequest {
    private String email;
    private String nickname;
    private String username;
    private String password;
    private String verifyPassword;
    private String userType;
    private List<Integer> userFacilityIds;
}
