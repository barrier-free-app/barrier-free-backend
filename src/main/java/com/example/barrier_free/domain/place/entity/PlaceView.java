package com.example.barrier_free.domain.place.entity;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import com.example.barrier_free.domain.place.enums.ImageType;
import com.example.barrier_free.domain.place.enums.PlaceType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Getter;

@Getter
@Entity
@Immutable
@Subselect("SELECT * FROM place_view")
public class PlaceView {

	@Id
	private Long id;

	private String name;
	private String region;
	private String address;
	private ImageType imageType;
	@Column(columnDefinition = "TEXT")
	private String description;

	@Enumerated(EnumType.STRING)
	private PlaceType placeType;

}
