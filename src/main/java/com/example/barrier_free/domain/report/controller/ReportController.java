package com.example.barrier_free.domain.report.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.barrier_free.domain.report.dto.ReportRequestDto;
import com.example.barrier_free.domain.report.service.ReportService;
import com.example.barrier_free.global.response.ApiResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/reports")
public class ReportController {

	private final ReportService reportService;

	@PostMapping
	public ResponseEntity<?> createReport(@RequestBody ReportRequestDto dto) {
		Long id = reportService.createReport(dto);
		return ApiResponse.success(HttpStatus.CREATED, Map.of("reportId", id));
	}

}
