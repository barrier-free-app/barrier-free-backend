package com.example.barrier_free.domain.facility.repository.mapFacility;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.barrier_free.domain.map.entity.Map;

public interface MapFacilityRepository extends JpaRepository<Map, Long>, MapFacilityRepositoryCustom {
}
