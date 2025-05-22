package com.example.barrier_free.domain.facility.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.barrier_free.domain.facility.entity.Facility;

public interface FacilityRepository extends JpaRepository<Facility, Long> {

	List<Facility> findAllByIdIn(List<Integer> ids);
}
