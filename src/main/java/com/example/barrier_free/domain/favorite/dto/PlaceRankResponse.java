package com.example.barrier_free.domain.favorite.dto;

import java.util.List;

import com.example.barrier_free.domain.favorite.entity.WeeklyRank;
import com.example.barrier_free.domain.map.entity.Map;
import com.example.barrier_free.domain.report.entity.Report;
import com.example.barrier_free.global.common.PlaceType;
import com.example.barrier_free.global.exception.CustomException;
import com.example.barrier_free.global.response.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlaceRankResponse {
	private Long placeId;
	private PlaceType placeType;
	private String name;
	private String region;
	private String description;
	private List<Integer> facility;
	private int imageType;

	public static PlaceRankResponse from(WeeklyRank weeklyRank) {
		if (weeklyRank.getMap() != null) {
			Map map = weeklyRank.getMap();
			return new PlaceRankResponse(
				map.getId(),
				PlaceType.map,
				map.getName(),
				map.getRegion(),
				map.getDescription(),
				map.getFacility(),
				map.getImageType().getCode()
			);
		} else if (weeklyRank.getReport() != null) {
			Report report = weeklyRank.getReport();
			return new PlaceRankResponse(
				report.getId(),
				PlaceType.report,
				report.getName(),
				report.getRegion(),
				report.getDescription(),
				report.getFacility(),
				report.getImageType().getCode()
			);
		} else {
			throw new CustomException(ErrorCode.INVALID_WEEKLY_RANK);
		}

	}
}
