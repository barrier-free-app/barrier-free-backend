package com.example.barrier_free.domain.review.repository;

import java.util.List;

import com.example.barrier_free.domain.review.entity.Review;

public interface ReviewRepositoryCustom {
	List<Review> findAllWithDetailsByUserId(Long userId);
}
