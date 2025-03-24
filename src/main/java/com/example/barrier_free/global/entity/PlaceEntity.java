package com.example.barrier_free.global.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class PlaceEntity extends BaseEntity {
	private String name;
	private String address;
	private String contact;
	@Column(name = "opening_hours")
	private String openingHours;

	@Column(length = 500)
	private String thumbnail;

	@Column(length = 500)
	private String homepage;

}
