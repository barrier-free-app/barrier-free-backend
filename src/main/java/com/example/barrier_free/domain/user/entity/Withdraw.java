package com.example.barrier_free.domain.user.entity;

import com.example.barrier_free.global.common.BaseEntity;
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
public class Withdraw {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reason; // 유저 탈퇴 이유
    private Long userId;  // 탈퇴 당시 유저 id
    private String email; // 탈퇴 당시 유저 이메일

    private LocalDateTime createdAt;

    public Withdraw(String reason, Long userId, String email) {
        this.reason = reason;
        this.userId = userId;
        this.email = email;
        this.createdAt = LocalDateTime.now();
    }
}
