package com.example.barrier_free.domain.favorite.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.barrier_free.domain.favorite.entity.Favorite;

public interface FavoriteRepository extends JpaRepository<Favorite, Long>, FavoriteRepositoryCustom {

	Optional<Favorite> findByUserIdAndReportId(Long userId, Long reportPlaceId);

	Optional<Favorite> findByUserIdAndMapId(Long userId, Long mapId);

	boolean existsByUserIdAndMapId(Long userId, Long placeId);

	boolean existsByUserIdAndReportId(Long userId, Long placeId);
}
