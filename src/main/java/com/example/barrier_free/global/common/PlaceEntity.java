package com.example.barrier_free.global.common;

import com.example.barrier_free.domain.place.converter.ImageTypeConverter;
import com.example.barrier_free.domain.place.enums.ImageType;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class PlaceEntity extends BaseEntity {
	private String name;
	private String address;
	private String contact;

	@Convert(converter = ImageTypeConverter.class)
	private ImageType imageType;
	private String region;
	private double latitude;
	private double longitude;

	@Column(columnDefinition = "TEXT")
	private String description;

	@Column(name = "opening_hours")
	private String openingHours;

	@Column(length = 500)
	private String thumbnail;

	@Column(length = 500)
	private String homepage;

}
