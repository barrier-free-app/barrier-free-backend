package com.example.barrier_free.domain.user.service;

import com.example.barrier_free.domain.facility.entity.Facility;
import com.example.barrier_free.domain.facility.repository.FacilityRepository;
import com.example.barrier_free.domain.user.UserRepository;
import com.example.barrier_free.domain.user.VerificationCodeRepository;
import com.example.barrier_free.domain.user.converter.UserConverter;
import com.example.barrier_free.domain.user.dto.*;
import com.example.barrier_free.domain.user.entity.User;
import com.example.barrier_free.domain.user.entity.VerificationCode;
import com.example.barrier_free.domain.user.enums.SocialType;
import com.example.barrier_free.domain.user.enums.UserType;
import com.example.barrier_free.global.exception.CustomException;
import com.example.barrier_free.global.jwt.JwtManager;
import com.example.barrier_free.global.response.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
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
    private final JavaMailSender mailSender;

    // 회원가입
    @Transactional
    public UserResponse signupUser(SignupRequest signupRequest) {

        // 일반 로그인 이메일 중복 확인
        String email = signupRequest.getEmail();
        if (userRepository.existsByEmailAndSocialType(email, SocialType.GENERAL)) {
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTED);
        }

        // 이메일 인증 여부 확인
        VerificationCode verificationCode = verificationCodeRepository.findTopByEmailOrderByCreatedAtDesc(email)
                .orElseThrow(() -> new CustomException(ErrorCode.VERIFICATION_CODE_NOT_FOUND));
        if (!verificationCode.isVerified()) {
            throw new CustomException(ErrorCode.EMAIL_UNAUTHORIZED);
        }

        String nickname = signupRequest.getNickname();
        String username = signupRequest.getUsername();
        String password = signupRequest.getPassword();

        // 아이디 및 비밀번호 유효성 검사
        validateInput(signupRequest.getUsername());
        validateInput(signupRequest.getPassword());
        if (!signupRequest.getPassword().equals(signupRequest.getVerifyPassword())) {
            throw new CustomException(ErrorCode.USER_PASSWORD_MISMATCH);
        }
        password = passwordEncoder.encode(password); // 비밀번호 인코딩 진행

        // 닉네임 및 아이디 중복 확인
        if (userRepository.existsByNickname(nickname)) throw new CustomException(ErrorCode.USER_NICKNAME_DUPLICATE);
        if (userRepository.existsByUsername(signupRequest.getUsername())) throw new CustomException(ErrorCode.USER_USERNAME_DUPLICATE);

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
                if (input == null || input.isBlank()) throw new CustomException(ErrorCode.USER_INPUT_REQUIRED);
                else if (userRepository.existsByNickname(input)) {
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

    @Transactional
    public String findAccount(String type, EmailRequest emailRequest) {

        String email = emailRequest.getEmail();

        // 유저 존재하는지 확인
        User user = userRepository.findByEmailAndSocialType(email, SocialType.GENERAL)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        switch (type) {
            case "username" -> {
                generateFindMailFormat(email, "아이디", user.getUsername());
                return "아이디가 이메일로 발송되었습니다.";
            }
            case "password" -> {
                // 16자 임시 비번 생성 및 저장
                String temp = generatePassword(16);
                user.updatePassword(passwordEncoder.encode(temp));
                user.resetIncorrectTimes(); // 틀린 횟수 초기화

                generateFindMailFormat(email, "비밀번호", temp);
                return "비밀번호가 이메일로 발송되었습니다.";
            }
            default -> {
                String message = "유효하지 않은 찾기 타입입니다. (username/password 중 입력 바랍니다.)";
                throw new CustomException(ErrorCode.USER_INVALID_TYPE, message); // 다른 타입 입력한 경우
            }
        }
    }

    // 찾기 이메일 전송 형식
    private void generateFindMailFormat(String email, String type, String input) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("[모두로] " + type + " 찾기 안내");
        message.setText("회원님의 " + type + "는 다음과 같습니다.\n\n" + input);
        mailSender.send(message);
    }

    // 임시 비밀번호
    private String generatePassword(int length) {
        StringBuilder code = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < length; i++) {
            int type = random.nextInt(3);

            switch (type) {
                case 0 -> code.append((char)(random.nextInt(26) + 'a')); // 소문자
                case 1 -> code.append((char)(random.nextInt(26) + 'A')); // 대문자
                case 2 -> code.append(random.nextInt(10));               // 숫자 0~9
            }
        }

        return code.toString();
    }

    // 비밀번호 변경
    @Transactional
    public String updatePassword(Long userId, PasswordRequest passwordRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        String password = passwordRequest.getPassword();

        // 비밀번호 유효성 검사
        validateInput(password);
        if (!password.equals(passwordRequest.getVerifyPassword())) {
            throw new CustomException(ErrorCode.USER_PASSWORD_MISMATCH);
        }

        // 새 비밀번호 저장
        user.updatePassword(passwordEncoder.encode(password));
        return "비밀번호가 변경되었습니다.";
    }
}
