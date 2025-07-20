package com.example.barrier_free.domain.report.dto;

import com.example.barrier_free.domain.user.entity.User;
import com.example.barrier_free.global.common.geo.CoordinatesAndRegion;

public record ReportContext(ReportRequestDto dto, User user, CoordinatesAndRegion coordinatesAndRegion) {

}
