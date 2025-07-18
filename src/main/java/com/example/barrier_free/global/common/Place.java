package com.example.barrier_free.global.common;

import java.util.List;

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

	int getImageType();

	String getRegion();

	double getLatitude();

	double getLongitude();
}
