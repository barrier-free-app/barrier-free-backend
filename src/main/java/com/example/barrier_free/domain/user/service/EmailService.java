package com.example.barrier_free.domain.user.service;

import com.example.barrier_free.domain.user.VerificationCodeRepository;
import com.example.barrier_free.domain.user.UserRepository;
import com.example.barrier_free.domain.user.dto.EmailRequest;
import com.example.barrier_free.domain.user.dto.EmailCodeRequest;
import com.example.barrier_free.domain.user.entity.VerificationCode;
import com.example.barrier_free.domain.user.enums.SocialType;
import com.example.barrier_free.global.exception.CustomException;
import com.example.barrier_free.global.response.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class EmailService {

    private final UserRepository userRepository;
    private final VerificationCodeRepository verificationCodeRepository;
    private final JavaMailSender mailSender;

    // 메일로 전송
    @Transactional
    public String sendToEmail(EmailRequest emailRequest) {

        String email = emailRequest.getEmail();

        // 일반 로그인 이메일 중복 확인
        if (userRepository.existsByEmailAndSocialType(email, SocialType.GENERAL)) {
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTED);
        }

        // 4자리 인증코드 생성
        String code = generateVeriCode(4);
        VerificationCode verificationCode = new VerificationCode(email, code);
        verificationCodeRepository.save(verificationCode);

        // 메일 전송
        generateEmailFormat(email, code);
        return "인증코드 메일 전송 완료";
    }

    // 이메일 전송 형식
    private void generateEmailFormat(String email, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("[모두로] 이메일 인증코드 안내");
        message.setText("아래 인증코드를 입력해주세요:\n\n" + code);
        mailSender.send(message);
    }

    // 인증 코드 생성
    private String generateVeriCode(int length) {

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

    // 메일 인증
    @Transactional
    public String verifyCode(EmailCodeRequest emailCodeRequest) {

        // 이메일의 최근 인증번호
        VerificationCode verificationCode =
                verificationCodeRepository.findTopByEmailOrderByCreatedAtDesc(emailCodeRequest.getEmail())
                        .orElseThrow(() -> new CustomException(ErrorCode.VERIFICATION_CODE_NOT_FOUND));

        // 인증 완료된 경우
        if (verificationCode.isVerified()) throw new CustomException(ErrorCode.VERIFICATION_CODE_ALREADY_VERIFIED);

        // 인증 번호 다른 경우
        else if (!verificationCode.getVerificationCode()
                .equals(emailCodeRequest.getVerificationCode()))
            throw new CustomException(ErrorCode.VERIFICATION_CODE_MISMATCH);

        // 유효 기간 완료된 경우 (5분)
        else if (verificationCode.getCreatedAt().isBefore(LocalDateTime.now().minusMinutes(5)))
            throw new CustomException(ErrorCode.VERIFICATION_CODE_EXPIRED);

        else verificationCode.setVerified();

        return "인증되었습니다. 상태-" + verificationCode.isVerified();
    }
}
