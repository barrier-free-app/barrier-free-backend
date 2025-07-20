package com.example.barrier_free.domain.user.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public class UpdateUserFacilitiesRequest {
	private List<Integer> facilityIds = new ArrayList<>();
}
