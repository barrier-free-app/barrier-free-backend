package com.example.barrier_free.domain.map.entity;

import java.util.ArrayList;
import java.util.List;

import com.example.barrier_free.domain.facility.entity.MapFacility;
import com.example.barrier_free.domain.favorite.entity.Favorite;
import com.example.barrier_free.global.entity.PlaceEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Map extends PlaceEntity {
	@OneToMany(mappedBy = "map", cascade = CascadeType.ALL, orphanRemoval = true)
	private final List<MapFacility> mapFacilities = new ArrayList<>();
	@OneToMany(mappedBy = "map", cascade = CascadeType.ALL, orphanRemoval = true)
	private final List<Favorite> favorites = new ArrayList<>();
	@OneToMany(mappedBy = "map", cascade = CascadeType.ALL, orphanRemoval = true)
	private final List<MapImage> mapImages = new ArrayList<>();
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private float totalRating;
	private float latitude;
	private float altitude;

}
