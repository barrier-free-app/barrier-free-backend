package com.example.barrier_free.domain.report.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.barrier_free.domain.report.entity.Report;
import com.example.barrier_free.domain.report.entity.Vote;
import com.example.barrier_free.domain.report.enums.VoteType;
import com.example.barrier_free.domain.user.entity.User;

public interface VoteRepository extends JpaRepository<Vote, Long> {
	boolean existsByUserAndReport(User user, Report report);

	int countByReportAndVoteType(Report report, VoteType voteType);

	Optional<Vote> findByUserAndReport(User user, Report report);
}