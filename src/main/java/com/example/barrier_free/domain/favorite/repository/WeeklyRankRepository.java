package com.example.barrier_free.domain.favorite.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.barrier_free.domain.favorite.entity.WeeklyRank;

public interface WeeklyRankRepository extends JpaRepository<WeeklyRank, Long> {

	List<WeeklyRank> findTop3ByYearAndWeekOrderByFavoriteCountDesc(int year, int week);

	void deleteByYearAndWeek(int year, int week);
}
