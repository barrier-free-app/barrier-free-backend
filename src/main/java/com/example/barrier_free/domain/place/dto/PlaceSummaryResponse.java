package com.example.barrier_free.domain.place.dto;

import java.util.List;

import com.example.barrier_free.global.common.PlaceType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PlaceSummaryResponse {
	private String name;
	private String description;
	private String address;
	private List<Integer> facilities;
	private PlaceType placeType;
	private int imageType;
	private boolean favorite;
}
