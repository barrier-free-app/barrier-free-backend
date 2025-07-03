package com.example.barrier_free.domain.favorite.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.barrier_free.domain.favorite.entity.Favorite;

public interface FavoriteRepository extends JpaRepository<Favorite, Long>, FavoriteRepositoryCustom {
	//유저가 해당 장소에 좋아요를 눌렀는지 확인하는 쿼리-제보장소와 오피셜 장소의 구분이 필요
	Optional<Favorite> findByUserIdAndReportId(Long userId, Long reportPlaceId);

	Optional<Favorite> findByUserIdAndMapId(Long userId, Long mapId);

}
