package com.example.barrier_free.domain.map.entity;

import java.util.ArrayList;
import java.util.List;

import com.example.barrier_free.domain.facility.entity.MapFacility;
import com.example.barrier_free.global.entity.PlaceEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Map extends PlaceEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private float totalRating;
	private float latitude;
	private float altitude;

	@OneToMany(mappedBy = "map", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<MapFacility> mapFacilities = new ArrayList<>();

}
