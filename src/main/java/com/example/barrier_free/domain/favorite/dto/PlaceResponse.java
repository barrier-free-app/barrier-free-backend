package com.example.barrier_free.domain.favorite.dto;

import java.util.List;

import com.example.barrier_free.domain.favorite.entity.WeeklyRank;
import com.example.barrier_free.global.common.Place;
import com.example.barrier_free.global.common.PlaceType;
import com.example.barrier_free.global.exception.CustomException;
import com.example.barrier_free.global.response.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlaceResponse {
	private Long placeId;
	private PlaceType placeType;
	private String name;
	private String description;
	private List<Integer> facility;
	private int imageType;

	public static PlaceResponse fromPlace(Place place) {
		return new PlaceResponse(
			place.getId(),
			place.getPlaceType(),
			place.getName(),
			place.getDescription(),
			place.getFacility(),
			place.getImageType()
		);
	}

	public static PlaceResponse from(WeeklyRank weeklyRank) {
		if (weeklyRank.getMap() != null) {
			return new PlaceResponse(
				weeklyRank.getMap().getId(),
				PlaceType.map,
				weeklyRank.getMap().getName(),
				weeklyRank.getMap().getDescription(),
				weeklyRank.getMap().getFacility(),
				weeklyRank.getMap().getImageType()
			);
		} else if (weeklyRank.getReport() != null) {
			return new PlaceResponse(
				weeklyRank.getReport().getId(),
				PlaceType.report,
				weeklyRank.getReport().getName(),
				weeklyRank.getReport().getDescription(),
				weeklyRank.getReport().getFacility(),
				weeklyRank.getReport().getImageType()
			);
		} else {
			throw new CustomException(ErrorCode.INVALID_WEEKLY_RANK);
		}

	}
}
