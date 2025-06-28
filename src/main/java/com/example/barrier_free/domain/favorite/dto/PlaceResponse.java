package com.example.barrier_free.domain.favorite.dto;

import java.util.List;

import com.example.barrier_free.domain.favorite.entity.MonthlyRank;
import com.example.barrier_free.global.common.Place;
import com.example.barrier_free.global.common.PlaceType;

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

	public static PlaceResponse fromPlace(Place place) {
		return new PlaceResponse(
			place.getId(),
			place.getPlaceType(),
			place.getName(),
			place.getDescription(),
			place.getFacility()
		);
	}

	public static PlaceResponse from(MonthlyRank monthlyRank) {
		if (monthlyRank.getMap() != null) {
			return new PlaceResponse(
				monthlyRank.getMap().getId(),
				PlaceType.map,
				monthlyRank.getMap().getName(),
				monthlyRank.getMap().getDescription(),
				monthlyRank.getMap().getFacility()
			);
		} else if (monthlyRank.getReport() != null) {
			return new PlaceResponse(
				monthlyRank.getReport().getId(),
				PlaceType.report,
				monthlyRank.getReport().getName(),
				monthlyRank.getReport().getDescription(),
				monthlyRank.getReport().getFacility()
			);
		} else {
			throw new IllegalStateException("MonthlyRank이상");
		}

	}
}
