package com.example.barrier_free.domain.user;

import com.example.barrier_free.domain.user.entity.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
    Optional<VerificationCode> findTopByEmailOrderByCreatedAtDesc(String email);
}
