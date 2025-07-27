package com.example.barrier_free.domain.favorite.dto;

import com.example.barrier_free.domain.place.enums.PlaceType;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class FavoriteRequestDto {
	@NotNull
	private Long placeId;
	@NotNull
	private PlaceType type;

}
