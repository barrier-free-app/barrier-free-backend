package com.example.barrier_free.domain.report.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.barrier_free.domain.report.dto.ReportRequestDto;
import com.example.barrier_free.domain.report.dto.VoteInfoResponse;
import com.example.barrier_free.domain.report.dto.VoteRequestDto;
import com.example.barrier_free.domain.report.service.ReportService;
import com.example.barrier_free.global.response.ApiResponse;
import com.example.barrier_free.global.response.SuccessCode;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/reports")
@Tag(name = "제보장소", description = "장소 제보 및 장소 투표 관련 API")
public class ReportController {

	private final ReportService reportService;

	@Operation(
		summary = "장소 제보 생성 API")
	@PostMapping
	public ApiResponse<?> createReport(@RequestBody @Valid ReportRequestDto dto) {
		Long id = reportService.createReport(dto);
		return ApiResponse.success(SuccessCode.CREATED, Map.of("reportId", Long.valueOf(id)));
	}

	@Operation(
		summary = "제보 장소에 투표 생성 API",
		description = "voteType 은 UP, DOWN 두 가지입니다."
	)
	@PostMapping("/{reportId}/vote")
	public ApiResponse<?> createVote(@PathVariable Long reportId, @RequestBody @Valid VoteRequestDto dto) {
		Long voteId = reportService.createVote(dto, reportId);
		return ApiResponse.success(SuccessCode.CREATED, Map.of("voteId", voteId));
	}

	@Operation(
		summary = "제보 장소에 대한 투표 수 확인과 투표여부 API",
		description = " VoteTypeStatus 는 UP, DOWN, NONE 세가지 입니다. NONE은 아직 투표하지 않은 경우 입니다.")
	@GetMapping("/{reportId}/vote")
	public ApiResponse<?> getVoteInfo(@PathVariable Long reportId) {
		VoteInfoResponse voteInfo = reportService.getReportVoteInfo(reportId);
		return ApiResponse.success(SuccessCode.OK, voteInfo);

	}

}
