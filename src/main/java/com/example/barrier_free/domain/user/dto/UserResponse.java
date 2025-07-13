package com.example.barrier_free.domain.user.dto;

import com.example.barrier_free.domain.facility.dto.FacilityResponse;
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
    private UserType userType;
    private List<FacilityResponse> userFacilities;
    private SocialType socialType;
}
