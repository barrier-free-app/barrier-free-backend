package com.example.barrier_free.domain.place.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PlaceSummaryResponse {
	// int imageType;
	String description;
	List<Integer> facilities;
	String address;
	String name;
	boolean favorite;
}
