package com.example.barrier_free.domain.place.dto;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.barrier_free.domain.place.entity.PlaceView;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PlaceSearchResponsePage {
	private List<PlaceSearchResponse> placeSearchResponses;
	private int page;
	private int size;
	private int totalPages;
	private long totalElements;
	private boolean hasNext;

	public static PlaceSearchResponsePage of(Page<PlaceView> placeViews, List<PlaceSearchResponse> content) {
		return new PlaceSearchResponsePage(
			content,
			placeViews.getNumber(),
			placeViews.getSize(),
			placeViews.getTotalPages(),
			placeViews.getTotalElements(),
			placeViews.hasNext()
		);
	}

}
