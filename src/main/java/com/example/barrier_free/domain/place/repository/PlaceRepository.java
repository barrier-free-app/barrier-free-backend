package com.example.barrier_free.domain.place.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.barrier_free.domain.place.entity.PlaceView;

public interface PlaceRepository extends JpaRepository<PlaceView, Long>, PlaceRepositoryCustom {

}
