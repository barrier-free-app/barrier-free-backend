package com.example.barrier_free.domain.facility.repository;

import static com.example.barrier_free.domain.facility.entity.QMapFacility.*;
import static com.example.barrier_free.domain.facility.entity.QReportFacility.*;

import java.util.ArrayList;
import java.util.List;

import com.example.barrier_free.domain.map.entity.Map;
import com.example.barrier_free.domain.map.entity.QMap;
import com.example.barrier_free.domain.report.entity.QReport;
import com.example.barrier_free.domain.report.entity.Report;
import com.example.barrier_free.global.common.Place;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PlaceFacilityRepositoryCustomImpl implements PlaceFacilityRepositoryCustom {

	private final JPAQueryFactory queryFactory;
	private static final QMap map = QMap.map;
	private static final QReport report = QReport.report;

	@Override
	public List<Place> findPlacesHavingAllFacilities(List<Integer> facilityIds) {

		List<Map> maps = queryFactory
			.select(mapFacility.map)
			.from(mapFacility)
			.where(mapFacility.facility.id.in(facilityIds))
			.groupBy(mapFacility.map)
			.having(mapFacility.facility.id.countDistinct().eq((long)facilityIds.size()))
			.fetch();

		List<Report> reports = queryFactory
			.select(reportFacility.report)
			.from(reportFacility)
			.where(reportFacility.facility.id.in(facilityIds))
			.groupBy(reportFacility.report)
			.having(reportFacility.facility.id.countDistinct().eq((long)facilityIds.size()))
			.fetch();
		List<Place> p = new ArrayList<>();
		p.addAll(maps);
		p.addAll(reports);
		return p;
	}

}
