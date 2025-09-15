package com.example.barrier_free.domain.place.dto;

import java.util.List;

import com.example.barrier_free.domain.place.enums.PlaceType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PlaceDetailResponse {
	private String name;
	private String description;
	private String address;
	private String openingHours;
	private List<Integer> facilities;
	private PlaceType placeType;
	private int imageType;
	private boolean favorite;

	private int reviewCount;
	private double avgRating;
}
