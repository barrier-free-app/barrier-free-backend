package com.example.barrier_free.domain.place.repository;

import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.example.barrier_free.domain.facility.entity.QMapFacility;
import com.example.barrier_free.domain.facility.entity.QReportFacility;
import com.example.barrier_free.domain.map.entity.QMap;
import com.example.barrier_free.domain.place.entity.PlaceView;
import com.example.barrier_free.domain.place.entity.QPlaceView;
import com.example.barrier_free.domain.place.enums.PlaceType;
import com.example.barrier_free.domain.report.entity.QReport;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PlaceRepositoryImpl implements PlaceRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	private final QMap map = QMap.map;
	private final QReport report = QReport.report;
	private final QMapFacility mapFacility = QMapFacility.mapFacility;
	private final QReportFacility reportFacility = QReportFacility.reportFacility;
	private final QPlaceView placeView = QPlaceView.placeView;

	//1. facility 필터링 mapId
	public List<Long> getFilteredMapsId(List<Integer> facilityIds) {

		return queryFactory
			.select(map.id)
			.from(mapFacility)
			.join(mapFacility.map, map)
			.where(mapFacility.facility.id.in(facilityIds))
			.groupBy(map.id)
			.having(mapFacility.facility.id.countDistinct().eq((long)facilityIds.size()))
			.fetch();
	}

	//2. reportId
	private List<Long> getFilteredReportsId(List<Integer> facilityIds) {

		return queryFactory
			.select(report.id)
			.from(reportFacility)
			.join(reportFacility.report, report)
			.where(reportFacility.facility.id.in(facilityIds))
			.groupBy(report.id)
			.having(reportFacility.facility.id.countDistinct().eq((long)facilityIds.size()))
			.fetch();

	}

	private boolean hasFacilityFilter(List<Integer> facilityIds) {
		return facilityIds != null && !facilityIds.isEmpty();
	}

	private boolean isEmptyFilteredPlaces(List<Long> filteredMapsId, List<Long> filteredReportsId) {
		return filteredMapsId.isEmpty() && filteredReportsId.isEmpty();
	}

	private BooleanBuilder buildKeywordCondition(String keyword) {
		BooleanBuilder keywordWhere = new BooleanBuilder();
		if (keyword != null && !keyword.isBlank()) {
			keywordWhere.and(
				placeView.name.contains(keyword)
					.or(placeView.address.contains(keyword))
			);
		}
		return keywordWhere;
	}

	private boolean isNoSearchCondition(String keyword, List<Integer> facilityIds) {
		return (keyword == null || keyword.isBlank()) && !hasFacilityFilter(facilityIds);
	}

	private Page<PlaceView> findAllPlaceViews(Pageable pageable) {
		List<PlaceView> all = queryFactory
			.selectFrom(placeView)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = queryFactory
			.select(placeView.count())
			.from(placeView)
			.fetchOne();

		return new PageImpl<>(all, pageable, total != null ? total : 0);
	}

	private BooleanBuilder buildFacilityCondition(List<Long> filteredMapsId, List<Long> filteredReportsId) {
		BooleanBuilder builder = new BooleanBuilder();

		if (!filteredMapsId.isEmpty()) {
			builder.or(placeView.placeType.eq(PlaceType.map).and(placeView.id.in(filteredMapsId)));
		}

		if (!filteredReportsId.isEmpty()) {
			builder.or(placeView.placeType.eq(PlaceType.report).and(placeView.id.in(filteredReportsId)));
		}
		return builder;
	}

	public Page<PlaceView> searchPlacesByKeywordAndFacilities(String keyword, List<Integer> facilityIds,
		Pageable pageable) {
		//검색어랑 필터링 모두 존재안할시
		if (isNoSearchCondition(keyword, facilityIds)) {
			return findAllPlaceViews(pageable);
		}

		//검색어든 조건이든 둘 중 하나라도 존재
		BooleanBuilder finalCondition = buildKeywordCondition(keyword);
		if (hasFacilityFilter(facilityIds)) {
			List<Long> filteredMapsId = getFilteredMapsId(facilityIds);
			List<Long> filteredReportsId = getFilteredReportsId(facilityIds);

			if (isEmptyFilteredPlaces(filteredMapsId, filteredReportsId)) {
				return new PageImpl<>(Collections.emptyList(), pageable, 0);
			}
			finalCondition.and(buildFacilityCondition(filteredMapsId, filteredReportsId));
		}
		List<PlaceView> placeViews = queryFactory.
			select(placeView)
			.from(placeView)
			.where(finalCondition)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		long total = queryFactory
			.select(placeView.count())
			.from(placeView)
			.where(finalCondition)
			.fetchOne();

		return new PageImpl<>(placeViews, pageable, total);

	}

}
