package com.example.barrier_free.domain.user.controller;

import com.example.barrier_free.domain.user.dto.EmailVeriCodeRequest;
import com.example.barrier_free.domain.user.dto.EmailRequest;
import com.example.barrier_free.domain.user.service.EmailService;
import com.example.barrier_free.global.response.ApiResponse;
import com.example.barrier_free.global.response.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final EmailService emailService;

    // 이메일 인증코드 요청
    @PostMapping("/veri-code/send")
    public ApiResponse<?> sendEmailAuthCode(@RequestBody EmailRequest emailRequest) {
        return ApiResponse.success(SuccessCode.OK, emailService.sendToEmail(emailRequest));
    }

    // 이메일 인증코드 확인
    @PostMapping("/veri-code/verify")
    public ApiResponse<?> verifyEmailAuthCode(@RequestBody EmailVeriCodeRequest emailVeriCodeRequest) {
        return null;
    }
}
