package com.example.barrier_free.domain.place.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PlaceSearchCondition {
	private String keyword;
	private List<Integer> facilities = new ArrayList<>();

}
