package com.example.barrier_free.domain.facility.repository.reportFacility;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.barrier_free.domain.report.entity.Report;

public interface ReportFacilityRepository extends JpaRepository<Report, Long>, ReportFacilityRepositoryCustom {
}
