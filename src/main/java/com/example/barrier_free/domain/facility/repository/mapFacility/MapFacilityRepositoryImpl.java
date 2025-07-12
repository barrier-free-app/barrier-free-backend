package com.example.barrier_free.domain.facility.repository.mapFacility;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.barrier_free.domain.facility.entity.QMapFacility;
import com.example.barrier_free.domain.map.entity.QMap;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MapFacilityRepositoryImpl implements MapFacilityRepositoryCustom {
	private final QMap map = QMap.map;
	private final JPAQueryFactory queryFactory;
	private final QMapFacility mapFacility = QMapFacility.mapFacility;

	@Override
	public Map<Long, List<Integer>> findFacilitiesByMapIds(List<Long> mapIds) {

		//맵 아이디 당 편의시설을 List로 가져오기
		//1. map facility 에서 해당 map id 에 속하는 거 고르고 mapid로 그룹바이 하기
		//2. 그다음에 mapId, 랑 묶여진 걸로 Map 만들기?

		List<Tuple> mapIdWithFacilities = queryFactory
			.select(mapFacility.map.id, mapFacility.facility.id)
			.from(mapFacility)
			.where(mapFacility.map.id.in(mapIds))
			.fetch();

		Map<Long, List<Integer>> result = mapIdWithFacilities.stream()
			.collect(Collectors.groupingBy(tuple -> tuple.get(mapFacility.map.id),
				Collectors.mapping(
					tuple -> tuple.get(mapFacility.facility.id), Collectors.toList()
				)
			));
		return result;

	}
}
