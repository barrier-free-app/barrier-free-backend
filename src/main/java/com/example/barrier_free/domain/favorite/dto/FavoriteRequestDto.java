package com.example.barrier_free.domain.favorite.dto;

import com.example.barrier_free.global.common.PlaceType;

import lombok.Getter;

@Getter
public class FavoriteRequestDto {
	private Long userId;
	private Long placeId;
	private PlaceType type;

}
