package com.example.barrier_free.domain.user.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.example.barrier_free.domain.user.dto.*;
import com.example.barrier_free.domain.user.entity.Withdraw;
import com.example.barrier_free.domain.user.repository.WithdrawRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.barrier_free.domain.facility.entity.Facility;
import com.example.barrier_free.domain.facility.repository.FacilityRepository;
import com.example.barrier_free.domain.user.repository.UserRepository;
import com.example.barrier_free.domain.user.converter.UserConverter;
import com.example.barrier_free.domain.user.entity.User;
import com.example.barrier_free.domain.user.enums.UserType;
import com.example.barrier_free.global.exception.CustomException;
import com.example.barrier_free.global.jwt.JwtUserUtils;
import com.example.barrier_free.global.response.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final FacilityRepository facilityRepository;
	private final WithdrawRepository withdrawRepository;

	@Transactional
	public void updateUserFacilities(List<Integer> newFacilityIds) {
		Long currentUserId = JwtUserUtils.getCurrentUserId();
		User user = userRepository.findById(currentUserId)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

		List<Facility> facilities = newFacilityIds.stream()
				.map(id -> facilityRepository.findById(id)
						.orElseThrow(() -> new CustomException(ErrorCode.FACILITY_NOT_FOUND)))
				.collect(Collectors.toList());

		user.setUserFacilities(facilities);
	}


	// 닉네임 업데이트
	@Transactional
	public String updateUserNickname(UpdateNicknameRequest request) {
		Long userId = JwtUserUtils.getCurrentUserId();
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

		String nickname = request.getNickname();

		// 업데이트한지 한달이상 안 된 경우
		if (!isNicknameChangeAllowed(user)) {
			throw new CustomException(ErrorCode.NICKNAME_UPDATE_NOT_ALLOWED);
		}

		// 닉네입 중복 확인
		if (userRepository.existsByNickname(nickname))
			throw new CustomException(ErrorCode.USER_NICKNAME_DUPLICATE);

		user.updateNickname(nickname);
		return "닉네임이 변경되었습니다.";
	}

	// 닉네임 업데이트 가능 여부
	private boolean isNicknameChangeAllowed(User user) {
		LocalDateTime updatedAt = user.getNicknameUpdatedAt();
		if (updatedAt == null) {
			return true;
		}
		return updatedAt.plusMonths(1).isBefore(LocalDateTime.now());
	}

	// 사용자 유형 변경
	@Transactional
	public void updateUserType(UpdateUserType updateUserType) {
		Long userId = JwtUserUtils.getCurrentUserId();
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

		// string > enum 화
		user.updateUserType(UserType.fromString(updateUserType.getUserType()));
	}

    // 유저 정보 조회
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
		withdrawRepository.save(
				new Withdraw(
						deleteReasonRequest.getReason(),
						userId,
						user.getEmail()
				)
		);
        userRepository.delete(user);
        return "회원 탈퇴 완료되었습니다.";
    }
}
