package com.example.barrier_free.domain.place.dto;

import com.example.barrier_free.global.common.PlaceType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlaceMapMarkerResponse {
	private Long id;
	private String name;
	private double latitude;
	private double longitude;
	private String region;
	private PlaceType placeType;
}