package com.example.barrier_free.domain.favorite.entity;

import com.example.barrier_free.domain.map.entity.Map;
import com.example.barrier_free.domain.report.entity.Report;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class WeeklyRank {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private int year;
	private int week;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "map_id")
	private Map map;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "report_id")
	private Report report;

	private long favoriteCount;

	public static WeeklyRank from(Map map, int year, int week, long favoriteCount) {
		WeeklyRank rank = new WeeklyRank();
		rank.map = map;
		rank.year = year;
		rank.week = week;
		rank.favoriteCount = favoriteCount;
		return rank;
	}

	public static WeeklyRank from(Report report, int year, int week, long favoriteCount) {
		WeeklyRank rank = new WeeklyRank();
		rank.report = report;
		rank.year = year;
		rank.week = week;
		rank.favoriteCount = favoriteCount;
		return rank;
	}

}
