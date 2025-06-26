package com.example.barrier_free.domain.favorite.entity;

import java.time.YearMonth;

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
public class MonthlyRank {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String rankMonth;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "map_id")
	private Map map;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "report_id")
	private Report report;

	private long favoriteCount;

	public static MonthlyRank from(Map map, YearMonth month, long favoriteCount) {
		MonthlyRank rank = new MonthlyRank();
		rank.map = map;
		rank.rankMonth = month.toString();
		rank.favoriteCount = favoriteCount;
		return rank;
	}

	public static MonthlyRank from(Report report, YearMonth month, long favoriteCount) {
		MonthlyRank rank = new MonthlyRank();
		rank.report = report;
		rank.rankMonth = month.toString();
		rank.favoriteCount = favoriteCount;
		return rank;
	}

}
