package com.example.barrier_free.domain.place.dto;

import java.util.List;

import com.example.barrier_free.global.common.PlaceType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PlaceSearchResponse {
	private Long placeId;
	private PlaceType placeType;
	private List<Integer> facilities;
	private String region;
	private String name;
	private int imageType;

}
