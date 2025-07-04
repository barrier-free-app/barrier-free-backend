package com.example.barrier_free.domain.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.barrier_free.domain.report.entity.Report;
import com.example.barrier_free.domain.report.entity.Vote;
import com.example.barrier_free.domain.user.entity.User;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    boolean existsByUserAndReport(User user, Report report);
}