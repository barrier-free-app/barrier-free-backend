package com.example.barrier_free.domain.place.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.barrier_free.domain.place.entity.PlaceView;

public interface PlaceRepositoryCustom {
	Page<PlaceView> searchPlacesByKeywordAndFacilities(String keyword, List<Integer> facilityIds, Pageable pageable);
}
