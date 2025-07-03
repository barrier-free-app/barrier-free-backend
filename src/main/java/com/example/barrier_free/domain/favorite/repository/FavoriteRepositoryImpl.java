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
import com.querydsl.core.BooleanBuilder;
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
		BooleanBuilder mapWhere = new BooleanBuilder();
		mapWhere.and(favorite.user.id.eq(userId));

		BooleanBuilder reportWhere = new BooleanBuilder();
		reportWhere.and(favorite.user.id.eq(userId));

		if (facilityIds != null && !facilityIds.isEmpty()) {
			mapWhere.and(mapFacility.facility.id.in(facilityIds));
			reportWhere.and(reportFacility.facility.id.in(facilityIds));
		}

		List<Map> mapFavorites = queryFactory
			.select(map)
			.from(favorite)
			.join(favorite.map, map)
			.leftJoin(mapFacility).on(mapFacility.map.eq(map))
			.where(mapWhere)
			.groupBy(favorite.id)
			.having(
				facilityIds != null && !facilityIds.isEmpty()
					? mapFacility.facility.id.countDistinct().eq((long)facilityIds.size())
					: null
			)
			.fetch();

		List<Report> reportFavorites = queryFactory
			.select(report)
			.from(favorite)
			.join(favorite.report, report)
			.leftJoin(reportFacility).on(reportFacility.report.eq(report))
			.where(reportWhere)
			.groupBy(favorite.id)
			.having(
				facilityIds != null && !facilityIds.isEmpty()
					? reportFacility.facility.id.countDistinct().eq((long)facilityIds.size())
					: null
			)
			.fetch();

		return new FavoritePlaceGroup(mapFavorites, reportFavorites);
	}

}
