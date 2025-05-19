package com.example.barrier_free.domain.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.barrier_free.domain.report.entity.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
