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
    private String verificationCode;
    private boolean verified;

    private LocalDateTime createdAt;

    public VerificationCode(String email, String verificationCode) {
        this.email = email;
        this.verificationCode = verificationCode;
        this.verified = false;
        this.createdAt = LocalDateTime.now();
    }

    public void setVerified() {
        this.verified = true;
    }
}
