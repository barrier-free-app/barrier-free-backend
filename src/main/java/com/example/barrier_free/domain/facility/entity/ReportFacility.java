package com.example.barrier_free.domain.facility.entity;

import com.example.barrier_free.domain.report.entity.Report;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class ReportFacility {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "report_id")
	private Report report;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "facility_id")
	private Facility facility;

	public ReportFacility(Facility facility, Report report) {
		this.report = report;
		this.facility = facility;
	}

	public void setReport(Report report) {
		this.report = report;
	}

}
