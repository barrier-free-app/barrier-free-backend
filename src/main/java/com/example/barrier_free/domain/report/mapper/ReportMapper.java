package com.example.barrier_free.domain.report.mapper;

import com.example.barrier_free.domain.place.enums.ImageType;
import com.example.barrier_free.domain.report.dto.ReportContext;
import com.example.barrier_free.domain.report.dto.ReportRequestDto;
import com.example.barrier_free.domain.report.entity.Report;
import com.example.barrier_free.global.common.geo.CoordinatesAndRegion;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReportMapper {
	public static Report toEntity(ReportContext context) {
		ReportRequestDto reportRequest = context.dto();
		CoordinatesAndRegion coordinatesAndRegion = context.coordinatesAndRegion();

		return Report.builder()
			.name(reportRequest.getName())
			.address(reportRequest.getAddress())
			.description(reportRequest.getDescription())
			.contact(reportRequest.getContact())
			.openingHours(reportRequest.getOpeningHours())
			.thumbnail(reportRequest.getThumbnail())
			.region(coordinatesAndRegion.region())
			.latitude(coordinatesAndRegion.latitude())
			.longitude(coordinatesAndRegion.longitude())
			.imageType(ImageType.fromCode(reportRequest.getImageType()))
			.user(context.user())
			.build();
	}

}
