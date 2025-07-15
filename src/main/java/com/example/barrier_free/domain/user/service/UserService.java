package com.example.barrier_free.domain.user.service;

import com.example.barrier_free.domain.user.UserRepository;
import com.example.barrier_free.domain.user.converter.UserConverter;
import com.example.barrier_free.domain.user.dto.DeleteReasonRequest;
import com.example.barrier_free.domain.user.dto.PasswordRequest;
import com.example.barrier_free.domain.user.dto.UserResponse;
import com.example.barrier_free.domain.user.entity.User;
import com.example.barrier_free.global.exception.CustomException;
import com.example.barrier_free.global.response.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 내정보 테스트
    // TODO: 삭제
    public UserResponse getUser(Long userId) {
        // 유저 초기화
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return UserConverter.toUserResponse(user);
    }

    // 계정 삭제
    @Transactional
    public String deleteUser(Long userId, DeleteReasonRequest deleteReasonRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        userRepository.delete(user);
        return "((" + deleteReasonRequest.getReason() + "))의 이유로 탈퇴가 완료되었습니다.";
    }
}
