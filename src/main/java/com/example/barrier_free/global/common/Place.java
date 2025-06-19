package com.example.barrier_free.global.common;

import com.example.barrier_free.domain.review.entity.Review;

public interface Place {
	void attachTo(Review review);
}
