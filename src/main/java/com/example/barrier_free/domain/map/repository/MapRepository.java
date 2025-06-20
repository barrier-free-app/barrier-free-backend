package com.example.barrier_free.domain.map.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.barrier_free.domain.map.entity.Map;

public interface MapRepository extends JpaRepository<Map, Long> {
}
