package com.example.barrier_free.global.common;

import jakarta.persistence.Column;
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
	private int reviewCount;
	private double ratingSum;
	private String name;
	private String address;
	private String contact;
	@Column(name = "opening_hours")
	private String openingHours;

	@Column(length = 500)
	private String thumbnail;

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
