package com.example.barrier_free.domain.facility.repository;

import java.util.List;

import com.example.barrier_free.global.common.Place;

public interface PlaceFacilityRepositoryCustom {
	List<Place> findPlacesHavingAllFacilities(List<Integer> facilityIds);

}
