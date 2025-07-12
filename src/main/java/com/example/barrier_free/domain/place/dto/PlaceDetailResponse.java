package com.example.barrier_free.domain.place.dto;

import java.util.List;

import com.example.barrier_free.global.common.PlaceType;

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
	// private int imageType;
	private PlaceType placeType;
	private boolean favorite;
}
