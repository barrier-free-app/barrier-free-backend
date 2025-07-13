package com.example.barrier_free.domain.user.service;

import com.example.barrier_free.domain.facility.entity.Facility;
import com.example.barrier_free.domain.facility.repository.FacilityRepository;
import com.example.barrier_free.domain.user.UserRepository;
import com.example.barrier_free.domain.user.VerificationCodeRepository;
import com.example.barrier_free.domain.user.converter.UserConverter;
import com.example.barrier_free.domain.user.dto.LoginRequest;
import com.example.barrier_free.domain.user.dto.LoginResponse;
import com.example.barrier_free.domain.user.dto.SignupRequest;
import com.example.barrier_free.domain.user.dto.UserResponse;
import com.example.barrier_free.domain.user.entity.User;
import com.example.barrier_free.domain.user.entity.VerificationCode;
import com.example.barrier_free.domain.user.enums.SocialType;
import com.example.barrier_free.domain.user.enums.UserType;
import com.example.barrier_free.global.exception.CustomException;
import com.example.barrier_free.global.jwt.JwtManager;
import com.example.barrier_free.global.response.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final VerificationCodeRepository verificationCodeRepository;
    private final FacilityRepository facilityRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtManager jwtManager;

    // 회원가입
    @Transactional
    public UserResponse signupUser(SignupRequest signupRequest) {

        String email = signupRequest.getEmail();
        String nickname = signupRequest.getNickname();
        String username = signupRequest.getUsername();
        String password = signupRequest.getPassword();

        // 이메일 인증 여부 확인
        VerificationCode verificationCode = verificationCodeRepository.findTopByEmailOrderByCreatedAtDesc(email)
                .orElseThrow(() -> new CustomException(ErrorCode.VERIFICATION_CODE_NOT_FOUND));
        if (!verificationCode.isVerified()) {
            throw new CustomException(ErrorCode.EMAIL_UNAUTHORIZED);
        }

        // 아이디 및 비밀번호 유효성 검사
        validateInput(signupRequest.getUsername());
        validateInput(signupRequest.getPassword());
        if (!signupRequest.getPassword().equals(signupRequest.getVerifyPassword())) {
            throw new CustomException(ErrorCode.USER_PASSWORD_MISMATCH);
        }
        password = passwordEncoder.encode(password); // 비밀번호 인코딩 진행

        // 닉네임 및 아이디 중복 확인
        if (userRepository.existsByNickname(nickname))
            throw new CustomException(ErrorCode.USER_NICKNAME_DUPLICATE);
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            throw new CustomException(ErrorCode.USER_USERNAME_DUPLICATE);
        }

        // 사용자 유형 변환
        UserType userType = UserType.fromString(signupRequest.getUserType());

        // 유저 생성 및 db 저장
        User newUser = userRepository.save(new User(email, nickname, username, password, userType));

        // 사용자 편의시설 연결
        // TODO: 컨버터화
        List<Facility> facilities = signupRequest.getUserFacilityIds().stream()
                .map(id -> facilityRepository.findById(id)
                        .orElseThrow(() -> new CustomException(ErrorCode.FACILITY_NOT_FOUND)))
                .collect(Collectors.toList());
        newUser.setUserFacilities(facilities);
        userRepository.save(newUser);

        return UserConverter.toUserResponse(newUser);
    }

    // 유효성 검사 (영문+숫자 6~16자)
    private void validateInput(String input) {
        if (input == null || input.isBlank()) {
            throw new CustomException(ErrorCode.USER_INPUT_REQUIRED);
        }

        if (input.length() < 6 || input.length() > 16) {
            throw new CustomException(ErrorCode.USER_INVALID_LENGTH);
        }

        if (!Pattern.matches("^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{6,16}$", input)) {
            throw new CustomException(ErrorCode.USER_INVALID_FORMAT);
        }
    }

    // 닉네임/아이디 중복 확인
    public String verifyInputDuplicate(String type, String input) {
        switch (type) {
            case "nickname" -> {
                if (userRepository.existsByNickname(input)) {
                    throw new CustomException(ErrorCode.USER_NICKNAME_DUPLICATE);
                }
            }
            case "username" -> {
                validateInput(input); // 아이디 유효성 검사
                if (userRepository.existsByUsername(input)) {
                    throw new CustomException(ErrorCode.USER_USERNAME_DUPLICATE);
                }
            }
            default -> throw new CustomException(ErrorCode.USER_INVALID_TYPE); // 다른 타입 입력한 경우
        }

        return "사용 가능합니다.";
    }

    // 일반 로그인
    @Transactional(noRollbackFor = CustomException.class)
    public LoginResponse loginUser(LoginRequest loginRequest) {

        String input = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        validateInput(password); // 비밀번호 유효성 검사

        // 사용자 조회
        User user;
        if (input.contains("@")) {
            // 이메일 형식일 경우
            user = userRepository.findByEmailAndSocialType(input, SocialType.GENERAL)
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        } else {
            // 아이디 형식일 경우
            validateInput(input); // 아이디 유효성 검사
            user = userRepository.findByUsername(input)
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        }

        // 5회 이상 실패했는지 확인
        if (user.getIncorrectTimes() >= 5) {
            throw new CustomException(ErrorCode.USER_PASSWORD_TOO_MANY_FAILED);
        }

        // 비밀번호 일치 확인
        if (!passwordEncoder.matches(password, user.getPassword())) {
            user.setIncorrectTimes(); // 실패 횟수 +1

            int failed = user.getIncorrectTimes();
            String message = String.format("비밀번호가 일치하지 않습니다. (%d/5)", failed);
            throw new CustomException(ErrorCode.USER_PASSWORD_MISMATCH, message);
        }

        // 비밀번호 일치하면 (= 로그인 성공)
        user.resetIncorrectTimes(); // 실패 횟수 초기화

        // 토큰 발급
        String accessToken = jwtManager.createAccessToken(user.getId());
        String refreshToken = jwtManager.createRefreshToken(user.getId());

        // 유저에 토큰 저장
        user.setTokens(accessToken, refreshToken);
        return new LoginResponse(user.getId(), accessToken, refreshToken);
    }
}
