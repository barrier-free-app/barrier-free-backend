package com.example.barrier_free.domain.report.mapper;

import com.example.barrier_free.domain.report.dto.ReportRequestDto;
import com.example.barrier_free.domain.report.entity.Report;
import com.example.barrier_free.domain.user.entity.User;

public class ReportMapper {

	public static Report toEntity(ReportRequestDto dto, User user) {
		return Report.builder()
			.name(dto.getName())
			.address(dto.getAddress())
			.description(dto.getDescription())
			.contact(dto.getPhone())
			.openingHours(dto.getHours())
			.user(user)
			.build();
	}

}
