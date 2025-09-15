package com.example.barrier_free.domain.place.dto;

import com.example.barrier_free.global.common.Place;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlaceAndFavorite {
	private Place place;
	private boolean favorite;
}

