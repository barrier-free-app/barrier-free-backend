package com.example.barrier_free.domain.report.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.barrier_free.domain.facility.entity.Facility;
import com.example.barrier_free.domain.facility.entity.ReportFacility;
import com.example.barrier_free.domain.facility.repository.FacilityRepository;
import com.example.barrier_free.domain.report.dto.ReportRequestDto;
import com.example.barrier_free.domain.report.dto.VoteRequestDto;
import com.example.barrier_free.domain.report.entity.Report;
import com.example.barrier_free.domain.report.entity.Vote;
import com.example.barrier_free.domain.report.mapper.ReportMapper;
import com.example.barrier_free.domain.report.repository.ReportRepository;
import com.example.barrier_free.domain.report.repository.VoteRepository;
import com.example.barrier_free.domain.user.UserRepository;
import com.example.barrier_free.domain.user.entity.User;
import com.example.barrier_free.global.exception.CustomException;
import com.example.barrier_free.global.response.ErrorCode;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ReportService {

	private final VoteRepository voteRepository;
	private final ReportRepository reportRepository;
	private final UserRepository userRepository;
	private final FacilityRepository facilityRepository;

	@Transactional
	public Long createReport(ReportRequestDto dto) {
		User user = userRepository.findById(dto.getUserId())
			.orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

		Report report = ReportMapper.toEntity(dto, user);

		List<Facility> facilities = facilityRepository.findAllByIdIn(dto.getFacilities());

		//요청한 편의시설이 진짜 편의시설에 있는지 확인
		if (facilities.size() != dto.getFacilities().size()) {
			throw new CustomException(ErrorCode.NOT_FOUND_FACILITY);
		}

		for (Facility facility : facilities) {
			ReportFacility rf = new ReportFacility(facility, report);
			report.addReportFacility(rf);
		}

		reportRepository.save(report);
		return report.getId();
	}

	@Transactional
	public long createVote(VoteRequestDto dto, Long reportId) {
		User user = userRepository.findById(dto.getUserId())
			.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

		Report report = reportRepository.findById(reportId)
			.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_REPORT));
		if (voteRepository.existsByUserAndReport(user, report)) {
			throw new CustomException(ErrorCode.ALREADY_VOTE);
		}

		Vote vote = report.createVote(dto.getVoteType(), user);
		voteRepository.save(vote);
		return vote.getId();
	}
}
