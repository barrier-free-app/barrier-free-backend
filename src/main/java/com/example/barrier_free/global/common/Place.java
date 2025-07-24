package com.example.barrier_free.global.common;

import java.util.List;

import com.example.barrier_free.domain.place.enums.ImageType;
import com.example.barrier_free.domain.place.enums.PlaceType;
import com.example.barrier_free.domain.review.entity.Review;

public interface Place {
	void attachTo(Review review);

	Long getId();             // 장소 고유 ID

	String getName();         // 장소 이름

	String getAddress();

	String getDescription();  // 장소 부연설명

	String getOpeningHours();

	PlaceType getPlaceType(); //타입

	List<Integer> getFacility();     // 편의시설 정보

	ImageType getImageType();

	String getRegion();

	double getLatitude();

	double getLongitude();

	void increaseReviewStats(double rating);

	void decreaseReviewStats(double deletedRating);

	int getReviewCount();

	double getRatingSum();
}
