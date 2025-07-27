package com.example.barrier_free.domain.facility.repository.reportFacility;

import java.util.List;
import java.util.Map;

import com.example.barrier_free.domain.report.entity.Report;

public interface ReportFacilityRepositoryCustom {
	Map<Long, List<Integer>> findFacilitiesByReportIds(List<Long> reportIds);

	List<Report> findReportHavingAllFacilities(List<Integer> facilityIds);

}
