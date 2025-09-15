package com.example.barrier_free.domain.facility.repository.reportFacility;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.barrier_free.domain.facility.entity.QReportFacility;
import com.example.barrier_free.domain.report.entity.QReport;
import com.example.barrier_free.domain.report.entity.Report;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReportFacilityRepositoryImpl implements ReportFacilityRepositoryCustom {
	private final JPAQueryFactory queryFactory;
	private static final QReportFacility reportFacility = QReportFacility.reportFacility;
	private static final QReport report = QReport.report;

	@Override
	public Map<Long, List<Integer>> findFacilitiesByReportIds(List<Long> reportIds) {
		List<Tuple> reportIdsWithFacilities = queryFactory
			.select(reportFacility.report.id, reportFacility.facility.id)
			.from(reportFacility)
			.where(reportFacility.report.id.in(reportIds))
			.fetch();

		Map<Long, List<Integer>> result = reportIdsWithFacilities
			.stream()
			.collect(Collectors.groupingBy(
				tuple -> tuple.get(reportFacility.report.id),                         // 그룹 기준
				Collectors.mapping(                                                   // 그룹 안에서
					tuple -> tuple.get(reportFacility.facility.id),                  // value로 추출할 것
					Collectors.toList()                                              // value들을 리스트로 모음
				)
			));

		return result;
	}

	@Override
	public List<Report> findReportHavingAllFacilities(List<Integer> facilityIds) {
		return queryFactory
			.select(reportFacility.report)
			.from(reportFacility)
			.where(reportFacility.facility.id.in(facilityIds))
			.groupBy(reportFacility.report)
			.having(reportFacility.facility.id.countDistinct().eq((long)facilityIds.size()))
			.fetch();
	}
}
