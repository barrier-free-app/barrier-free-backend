package com.example.barrier_free.domain.favorite.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.barrier_free.domain.favorite.entity.MonthlyRank;

public interface MonthlyRankRepository extends JpaRepository<MonthlyRank, Long> {

	List<MonthlyRank> findByRankMonthOrderByFavoriteCountDesc(String beforeMonth);
}
