package com.example.barrier_free.domain.report.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.barrier_free.domain.report.dto.ReportRequestDto;
import com.example.barrier_free.domain.report.dto.VoteRequestDto;
import com.example.barrier_free.domain.report.service.ReportService;
import com.example.barrier_free.global.response.ApiResponse;
import com.example.barrier_free.global.response.SuccessCode;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/reports")
public class ReportController {

	private final ReportService reportService;

	@PostMapping
	public ApiResponse<?> createReport(@RequestBody ReportRequestDto dto) {
		Long id = reportService.createReport(dto);
		return ApiResponse.success(SuccessCode.CREATED, Map.of("reportId", Long.valueOf(id)));
	}

	@PostMapping("/{reportId}/vote")
	public ApiResponse<?> createVote(@RequestBody VoteRequestDto dto, @PathVariable Long reportId) {
		Long id = reportService.createVote(dto, reportId);
		return ApiResponse.success(SuccessCode.CREATED, Map.of("voteId", Long.valueOf(id)));

	}

}
