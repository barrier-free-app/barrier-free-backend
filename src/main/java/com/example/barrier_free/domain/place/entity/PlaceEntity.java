package com.example.barrier_free.domain.place.entity;

import com.example.barrier_free.domain.place.converter.ImageTypeConverter;
import com.example.barrier_free.domain.place.enums.ImageType;
import com.example.barrier_free.global.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class PlaceEntity extends BaseEntity {
	private int reviewCount;
	private double ratingSum;
	@Column(nullable = false)
	private String name;

	@Pattern(
		regexp = "^(0\\d{1,2})-(\\d{3,4})-(\\d{4})$")
	private String contact;

	@Column(nullable = false)
	private String address;

	@Column(nullable = false)
	private String region;

	@Column(nullable = false)
	private double latitude;

	@Column(nullable = false)
	private double longitude;
	@Convert(converter = ImageTypeConverter.class)
	@Column(nullable = false)
	private ImageType imageType;

	@Column(columnDefinition = "TEXT")
	private String description;

	@Column(name = "opening_hours")
	private String openingHours;

	@Column(length = 500)
	private String homepage;

	public void increaseReviewStats(double rating) {
		this.reviewCount++;
		this.ratingSum += rating;
	}

	public void decreaseReviewStats(double rating) {
		this.reviewCount = Math.max(0, this.reviewCount - 1);
		this.ratingSum = Math.max(0, this.ratingSum - rating);
	}

}
