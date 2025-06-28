package com.example.barrier_free.domain.favorite.repository;

import java.util.List;

import com.example.barrier_free.domain.facility.entity.QMapFacility;
import com.example.barrier_free.domain.facility.entity.QReportFacility;
import com.example.barrier_free.domain.favorite.dto.FavoritePlaceGroup;
import com.example.barrier_free.domain.favorite.entity.QFavorite;
import com.example.barrier_free.domain.map.entity.Map;
import com.example.barrier_free.domain.map.entity.QMap;
import com.example.barrier_free.domain.report.entity.QReport;
import com.example.barrier_free.domain.report.entity.Report;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FavoriteRepositoryImpl implements FavoriteRepositoryCustom {
	private JPAQueryFactory queryFactory;
	private static final QFavorite favorite = QFavorite.favorite;
	private static final QMap map = QMap.map;
	private static final QReport report = QReport.report;
	private static final QMapFacility mapFacility = QMapFacility.mapFacility;
	private static final QReportFacility reportFacility = QReportFacility.reportFacility;

	@Override
	public FavoritePlaceGroup findFilteredFavorites(Long userId, List<Integer> facilityIds) {

		List<Map> mapFavorites = queryFactory
			.select(map)
			.from(favorite)
			.join(favorite.map, map)
			.join(mapFacility).on(mapFacility.map.eq(map))
			.where(favorite.user.id.eq(userId)
				.and(mapFacility.facility.id.in(facilityIds)))
			.groupBy(favorite.id)
			.having(mapFacility.facility.id.countDistinct().eq((long)facilityIds.size()))
			.fetch();

		List<Report> reportFavorites = queryFactory
			.select(report)
			.from(favorite)
			.join(favorite.report, report)
			.join(reportFacility).on(reportFacility.report.eq(report))
			.where(favorite.user.id.eq(userId)
				.and(reportFacility.facility.id.in(facilityIds)))
			.groupBy(favorite.id)
			.having(reportFacility.facility.id.countDistinct().eq((long)facilityIds.size()))
			.fetch();
		return new FavoritePlaceGroup(mapFavorites, reportFavorites);
	}

	@Override
	public FavoritePlaceGroup findFavoriteByUserIdWithPlace(Long userId) {

		List<Map> mapFavorites = queryFactory
			.select(map)
			.from(favorite)
			.join(favorite.map, map)
			.where(favorite.user.id.eq(userId))
			.fetch();

		List<Report> reportFavorites = queryFactory
			.select(report)
			.from(favorite)
			.join(favorite.report, report)
			.where(favorite.user.id.eq(userId))
			.fetch();

		return new FavoritePlaceGroup(mapFavorites, reportFavorites);
	}
}
