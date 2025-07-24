package com.example.barrier_free.domain.favorite.dto;

import com.example.barrier_free.domain.place.enums.PlaceType;

import lombok.Getter;

@Getter
public class FavoriteRequestDto {
	private Long placeId;
	private PlaceType type;

}
