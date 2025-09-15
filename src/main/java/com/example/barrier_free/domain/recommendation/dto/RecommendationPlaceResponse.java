package com.example.barrier_free.domain.recommendation.dto;

import java.util.List;

import com.example.barrier_free.domain.place.enums.PlaceType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RecommendationPlaceResponse {
	private Long placeId;
	private PlaceType placeType;
	private String name;
	private String region;
	private String description;
	private List<Integer> facility;
	private int imageType;

}
