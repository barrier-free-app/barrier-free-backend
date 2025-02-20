package com.example.barrier_free.domain.facility.entity;

import com.example.barrier_free.domain.map.entity.Map;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class MapFacility {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "map_id")
	private Map map;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "facility_id")
	private Facility facility;
}
