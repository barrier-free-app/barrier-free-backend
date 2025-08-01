package com.example.barrier_free.domain.facility.repository.mapFacility;

import java.util.List;
import java.util.Map;

public interface MapFacilityRepositoryCustom {
	Map<Long, List<Integer>> findFacilitiesByMapIds(List<Long> mapIds);

	List<com.example.barrier_free.domain.map.entity.Map> findMapHavingAllFacilities(List<Integer> facilityIds);

}
