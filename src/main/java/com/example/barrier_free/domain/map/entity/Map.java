package com.example.barrier_free.domain.map.entity;

import java.util.ArrayList;
import java.util.List;

import com.example.barrier_free.domain.facility.entity.MapFacility;
import com.example.barrier_free.domain.favorite.entity.Favorite;
import com.example.barrier_free.domain.favorite.entity.WeeklyRank;
import com.example.barrier_free.domain.review.entity.Review;
import com.example.barrier_free.global.common.Place;
import com.example.barrier_free.global.common.PlaceEntity;
import com.example.barrier_free.global.common.PlaceType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Map extends PlaceEntity implements Place {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private float totalRating;
	private float latitude;
	private float longitude;

	@Column(columnDefinition = "TEXT")
	private String description;
	@OneToMany(mappedBy = "map", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<MapFacility> mapFacilities = new ArrayList<>();
	@OneToMany(mappedBy = "map", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Favorite> favorites = new ArrayList<>();
	@OneToMany(mappedBy = "map", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Review> reviews = new ArrayList<>();
	@OneToMany(mappedBy = "map", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<WeeklyRank> weeklyRanks = new ArrayList<>();

	@Override
	public void attachTo(Review review) {
		review.attachMap(this); // 리뷰에 맵 연결
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public PlaceType getPlaceType() {
		return PlaceType.map;
	}

	@Override
	//lazy로딩이므로 transactional 안에서만 사용
	public List<Integer> getFacility() {
		return mapFacilities.stream()
			.map(mapFacility -> mapFacility.getFacility().getId())
			.toList();
	}

}
