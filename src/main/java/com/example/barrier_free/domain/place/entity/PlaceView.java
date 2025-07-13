package com.example.barrier_free.domain.place.entity;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import com.example.barrier_free.global.common.PlaceType;

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
	private String address;
	private int imageType;

	@Enumerated(EnumType.STRING)
	private PlaceType placeType;
}
