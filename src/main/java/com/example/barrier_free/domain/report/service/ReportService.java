package com.example.barrier_free.domain.report.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.barrier_free.domain.facility.entity.Facility;
import com.example.barrier_free.domain.facility.entity.ReportFacility;
import com.example.barrier_free.domain.facility.repository.FacilityRepository;
import com.example.barrier_free.domain.report.dto.ReportRequestDto;
import com.example.barrier_free.domain.report.entity.Report;
import com.example.barrier_free.domain.report.mapper.ReportMapper;
import com.example.barrier_free.domain.report.repository.ReportRepository;
import com.example.barrier_free.domain.user.UserRepository;
import com.example.barrier_free.domain.user.entity.User;
import com.example.barrier_free.global.exception.CustomException;
import com.example.barrier_free.global.response.ErrorCode;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ReportService {

	private final ReportRepository reportRepository;
	private final UserRepository userRepository;
	private final FacilityRepository facilityRepository;

	public Long createReport(ReportRequestDto dto) {
		User user = userRepository.findById(dto.getUserId())
			.orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

		Report report = ReportMapper.toEntity(dto, user);

		List<Facility> facilities = facilityRepository.findAllByFacilityTypeIn(dto.getFacilities());
		Map<Integer, Facility> facilityMap = facilities.stream()
			.collect(Collectors.toMap(Facility::getFacilityType, f -> f));

		for (Integer type : dto.getFacilities()) {
			Facility facility = facilityMap.get(type);
			if (facility == null) {
				throw new CustomException(ErrorCode.NOT_FOUND_FACILITY);
			}
			ReportFacility rf = new ReportFacility(report, facility);
			report.addReportFacility(rf);
		}

		reportRepository.save(report);
		return report.getId();
	}
}
