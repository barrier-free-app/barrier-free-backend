package com.example.barrier_free.domain.review.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.barrier_free.domain.review.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
	@EntityGraph(attributePaths = {"user"})
	Page<Review> findByReportId(Long id, Pageable pageable);

	@EntityGraph(attributePaths = {"user"})
	Page<Review> findByMapId(Long id, Pageable pageable);

	@EntityGraph(attributePaths = {"map", "report", "user"})
	Page<Review> findByUserId(Long userId, Pageable pageable);
}
