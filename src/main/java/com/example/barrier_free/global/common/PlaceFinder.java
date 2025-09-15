package com.example.barrier_free.global.common;

import org.springframework.stereotype.Component;

import com.example.barrier_free.domain.map.repository.MapRepository;
import com.example.barrier_free.domain.place.enums.PlaceType;
import com.example.barrier_free.domain.report.repository.ReportRepository;
import com.example.barrier_free.global.exception.CustomException;
import com.example.barrier_free.global.response.ErrorCode;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PlaceFinder {

	private final ReportRepository reportRepository;
	private final MapRepository mapRepository;

	public Place findPlace(Long placeId, PlaceType placeType) {
		if (placeType == PlaceType.report) {
			return reportRepository.findById(placeId)
				.orElseThrow(() -> new CustomException(ErrorCode.PLACE_NOT_FOUND));
		} else {
			return mapRepository.findById(placeId)
				.orElseThrow(() -> new CustomException(ErrorCode.PLACE_NOT_FOUND));
		}
	}
}
