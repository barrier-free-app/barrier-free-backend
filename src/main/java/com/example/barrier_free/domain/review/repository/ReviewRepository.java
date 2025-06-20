package com.example.barrier_free.domain.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.barrier_free.domain.review.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
