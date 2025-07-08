package com.example.barrier_free.domain.user;

import com.example.barrier_free.domain.user.entity.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailAuthRepository extends JpaRepository<VerificationCode, Long> {
}
