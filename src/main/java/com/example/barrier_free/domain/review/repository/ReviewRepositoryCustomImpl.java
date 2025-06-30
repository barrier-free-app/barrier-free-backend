package com.example.barrier_free.domain.review.repository;

import java.util.List;

import com.example.barrier_free.domain.review.entity.Review;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<Review> findAllWithDetailsByUserId(Long userId) {
		QReview review = QReview.review;
		QMap map = QMap.map;
		QReport report = QReport.report;
		QReviewImage reviewImage = QReviewImage.reviewImage;

		return queryFactory
			.selectDistinct(review)
			.from(review)
			.leftJoin(review.map, map).fetchJoin()
			.leftJoin(review.report, report).fetchJoin()
			.leftJoin(review.reviewImages, reviewImage).fetchJoin()
			.where(review.user.id.eq(userId))
			.fetch();
	}
}
