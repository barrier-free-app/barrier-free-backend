package com.example.barrier_free.domain.user.converter;

import com.example.barrier_free.domain.facility.entity.UserFacility;
import com.example.barrier_free.domain.user.dto.UserResponse;
import com.example.barrier_free.domain.user.entity.User;
import com.example.barrier_free.domain.user.enums.SocialType;

import java.util.List;
import java.util.stream.Collectors;

public class UserConverter {

    public static UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profile(user.getProfile())
                .userType(user.getUserType())
                .userFacilities(toFacilityId(user.getUserFacilities()))
                .socialType(user.getSocialType())
                .build();
    }

    private static List<Integer> toFacilityId(List<UserFacility> facilities) {
        return facilities.stream()
                .map(uf -> uf.getFacility().getId())
                .collect(Collectors.toList());
    }

}
