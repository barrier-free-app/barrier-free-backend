package com.example.barrier_free.domain.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class VerificationCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String authCode;
    private boolean verified;

    private LocalDateTime createdAt;

    public VerificationCode(String email, String authCode) {
        this.email = email;
        this.authCode = authCode;
        this.verified = false;
        this.createdAt = LocalDateTime.now();
    }

    public void markVerified() {
        this.verified = true;
    }
}
