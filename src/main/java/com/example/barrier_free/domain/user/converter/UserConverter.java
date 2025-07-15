package com.example.barrier_free.domain.user.converter;

import com.example.barrier_free.domain.facility.dto.FacilityResponse;
import com.example.barrier_free.domain.facility.entity.Facility;
import com.example.barrier_free.domain.facility.entity.UserFacility;
import com.example.barrier_free.domain.user.dto.UserResponse;
import com.example.barrier_free.domain.user.entity.User;
import com.example.barrier_free.domain.user.enums.SocialType;

import java.util.List;
import java.util.stream.Collectors;

public class UserConverter {

    // 회원가입 후 유저 정보 반환
    public static UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .userType(user.getUserType())
                .userFacilities(toFacilityResponseList(user.getUserFacilities()))
                .socialType(user.getSocialType())
                .build();
    }

    // userFacilities > FacilityResponse(id+name 형식) 변환
    private static List<FacilityResponse> toFacilityResponseList(List<UserFacility> facilities) {
        return facilities.stream()
                .map(uf -> new FacilityResponse(
                        uf.getFacility().getId(),
                        uf.getFacility().getName()
                ))
                .collect(Collectors.toList());
    }

}
