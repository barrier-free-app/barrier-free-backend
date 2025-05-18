package com.example.barrier_free.domain.user.dto;

import com.example.barrier_free.domain.user.enums.SocialType;
import com.example.barrier_free.domain.user.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class UserResponse {
    private Long userId;
    private String email;
    private String nickname;
    private String profile;
    private UserType userType;
    private List<Integer> userFacilities;
    private SocialType socialType;
}
