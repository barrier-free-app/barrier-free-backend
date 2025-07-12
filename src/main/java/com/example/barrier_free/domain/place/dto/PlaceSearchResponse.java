package com.example.barrier_free.domain.place.dto;

import java.util.List;

import com.example.barrier_free.global.common.PlaceType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PlaceSearchResponse {
	Long placeId;
	PlaceType placeType;
	List<Integer> facilities;
	String address;
	String name;
	// int imageType;

}
