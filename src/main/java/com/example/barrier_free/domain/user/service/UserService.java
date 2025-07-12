package com.example.barrier_free.domain.user.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.barrier_free.domain.facility.entity.Facility;
import com.example.barrier_free.domain.facility.entity.UserFacility;
import com.example.barrier_free.domain.facility.repository.FacilityRepository;
import com.example.barrier_free.domain.user.UserRepository;
import com.example.barrier_free.domain.user.converter.UserConverter;
import com.example.barrier_free.domain.user.dto.NicknameCheckResponse;
import com.example.barrier_free.domain.user.dto.UserResponse;
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

	@Transactional
	public void updateUserFacilities(List<Integer> newFacilityIds) {
		Long currentUserId = JwtUserUtils.getCurrentUserId();
		User user = userRepository.findById(currentUserId)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

		List<Facility> facilities = facilityRepository.findAllByIdIn(newFacilityIds);
		if (facilities.size() != newFacilityIds.size()) {
			throw new CustomException(ErrorCode.NOT_FOUND_FACILITY);
		}

		List<UserFacility> newFacilities = facilities.stream()
			.map(facility -> UserFacility.of(facility))
			.collect(Collectors.toList());

		user.updateUserFacilities(newFacilities);

		userRepository.save(user);

	}

	public NicknameCheckResponse checkNicknameDuplicate(String nickname) {
		return new NicknameCheckResponse(!userRepository.existsByNickname(nickname));

	}

	@Transactional
	public void updateUserNickname(String nickname) {
		Long userId = JwtUserUtils.getCurrentUserId();
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

		if (!isNicknameChangeAllowed(user)) {
			throw new CustomException(ErrorCode.NICKNAME_UPDATE_NOT_ALLOWED);
		}

		user.updateNickname(nickname);

	}

	@Transactional
	public void updateUserType(UserType userType) {
		Long userId = JwtUserUtils.getCurrentUserId();
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

		user.updateUserType(userType);
	}

	private boolean isNicknameChangeAllowed(User user) {
		LocalDateTime updatedAt = user.getNicknameUpdatedAt();
		if (updatedAt == null) {
			return true;
		}
		return updatedAt.plusMonths(1).isBefore(LocalDateTime.now());
	}

	// 내정보 테스트
	// TODO: 삭제
	public UserResponse getUser(Long userId) {
		// 유저 초기화
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
		return UserConverter.toUserResponse(user);
	}

}
