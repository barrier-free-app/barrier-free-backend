package com.example.barrier_free.domain.facility.repository.reportFacility;

import java.util.List;
import java.util.Map;

public interface ReportFacilityRepositoryCustom {
	Map<Long, List<Integer>> findFacilitiesByReportIds(List<Long> reportIds);
}
