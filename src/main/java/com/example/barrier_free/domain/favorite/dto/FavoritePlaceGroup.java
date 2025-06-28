package com.example.barrier_free.domain.favorite.dto;

import java.util.List;

import com.example.barrier_free.domain.map.entity.Map;
import com.example.barrier_free.domain.report.entity.Report;

import lombok.Getter;

@Getter
public class FavoritePlaceGroup {
	private List<Map> mapFavorites;
	private List<Report> reportFavorites;

	public FavoritePlaceGroup(List<Map> mapFavorites, List<Report> reportFavorites) {
		this.mapFavorites = mapFavorites;
		this.reportFavorites = reportFavorites;
	}
}
