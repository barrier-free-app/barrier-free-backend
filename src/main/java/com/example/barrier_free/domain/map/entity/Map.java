package com.example.barrier_free.domain.map.entity;

import java.util.ArrayList;
import java.util.List;

import com.example.barrier_free.domain.facility.entity.MapFacility;
import com.example.barrier_free.domain.favorite.entity.Favorite;
import com.example.barrier_free.domain.review.entity.Review;
import com.example.barrier_free.global.common.Place;
import com.example.barrier_free.global.common.PlaceEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Map extends PlaceEntity implements Place {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private float totalRating;
	private float latitude;
	private float longitude;

	@OneToMany(mappedBy = "map", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<MapFacility> mapFacilities = new ArrayList<>();
	@OneToMany(mappedBy = "map", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Favorite> favorites = new ArrayList<>();
	@OneToMany(mappedBy = "map", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Review> reviews = new ArrayList<>();

	@Override
	public void attachTo(Review review) {
		review.attachMap(this); // 리뷰에 맵 연결
	}

}
