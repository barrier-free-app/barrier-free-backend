package com.example.barrier_free.domain.user.controller;

import com.example.barrier_free.domain.user.dto.EmailAuthCodeRequest;
import com.example.barrier_free.domain.user.dto.EmailRequest;
import com.example.barrier_free.domain.user.service.EmailService;
import com.example.barrier_free.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final EmailService emailService;

    // 이메일 인증코드 요청
    @PostMapping("/auth/email/code")
    public ApiResponse<?> createEmailAuthCode(@RequestBody EmailRequest email) {
        return null;
    }

    // 이메일 인증코드 확인
    @PostMapping("/auth/email/verify")
    public ApiResponse<?> verifyEmailAuthCode(@RequestBody EmailAuthCodeRequest emailAuthCode) {
        return null;
    }
}
