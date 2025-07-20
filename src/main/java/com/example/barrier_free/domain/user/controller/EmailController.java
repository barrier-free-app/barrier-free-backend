package com.example.barrier_free.domain.user.controller;

import com.example.barrier_free.domain.user.dto.EmailCodeRequest;
import com.example.barrier_free.domain.user.dto.EmailRequest;
import com.example.barrier_free.domain.user.service.EmailService;
import com.example.barrier_free.global.response.ApiResponse;
import com.example.barrier_free.global.response.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/email")
@RequiredArgsConstructor
@Tag(name = "1. 이메일", description = "이메일 인증코드 관련 API")
public class EmailController {

    private final EmailService emailService;

    // 이메일 인증코드 요청
    @PostMapping("/send")
    @Operation(summary = "이메일 인증코드 발송 API",
            description = """
                    해당 이메일 주소로 4자리 인증코드(숫자+문자)를 발송합니다.
                    
                    - email: 이메일 주소
                    """)
    public ApiResponse<?> sendEmailVeriCode(@RequestBody EmailRequest emailRequest) {
        return ApiResponse.success(SuccessCode.OK, emailService.sendToEmail(emailRequest));
    }

    // 이메일 인증코드 확인
    @PostMapping("/verify")
    @Operation(summary = "이메일 인증코드 확인 API",
            description = """
                    발송된 이메일 인증코드를 확인합니다. (이메일 인증코드 발송 API 우선)
                    
                    - email: 이메일 주소
                    - verificationCode: 인증코드 4자리 (5분 안에 입력)
                    """)
    public ApiResponse<?> verifyEmailVeriCode(@RequestBody EmailCodeRequest emailCodeRequest) {
        return ApiResponse.success(SuccessCode.OK, emailService.verifyCode(emailCodeRequest));
    }
}
