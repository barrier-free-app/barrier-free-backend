package com.example.barrier_free.domain.review.controller;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.barrier_free.domain.review.dto.PlaceReviewPageResponse;
import com.example.barrier_free.domain.review.dto.ReviewRequestDto;
import com.example.barrier_free.domain.review.dto.UserReviewPageResponse;
import com.example.barrier_free.domain.review.service.ReviewService;
import com.example.barrier_free.global.common.PlaceType;
import com.example.barrier_free.global.response.ApiResponse;
import com.example.barrier_free.global.response.SuccessCode;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class ReviewController {
	private final ReviewService reviewService;

	@GetMapping(value = "places/{placeId}/reviews")
	public ApiResponse<?> getReviews(
		@PathVariable long placeId,
		@RequestParam PlaceType type,
		@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
		PlaceReviewPageResponse reviews = reviewService.getReviewsByPlace(placeId, type, pageable);
		return ApiResponse.success(SuccessCode.OK, reviews);
	}

	@PostMapping(value = "places/{placeId}/reviews", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ApiResponse<?> createReview(@RequestPart("review") ReviewRequestDto requestDto,
		@RequestPart(value = "image", required = false) List<MultipartFile> images,
		@PathVariable long placeId,
		@RequestParam PlaceType type
	) {
		Long reviewId = reviewService.createReview(placeId, requestDto, images, type);
		return ApiResponse.success(SuccessCode.REVIEW_CREATED, Map.of("reviewId", reviewId));
	}

	@DeleteMapping(value = "users/{userId}/reviews/{reviewId}")
	public ApiResponse<?> deleteReview(
		@PathVariable long userId
		, @PathVariable long reviewId
	) {
		reviewService.deleteReview(userId, reviewId);
		return ApiResponse.success(SuccessCode.OK, "삭제완료");
	}

	@GetMapping(value = "users/{userId}/reviews")
	public ApiResponse<?> getUserReviews(
		@PathVariable long userId,
		@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
	) {
		UserReviewPageResponse reviews = reviewService.getReviewsByUser(userId, pageable);
		return ApiResponse.success(SuccessCode.OK, reviews);
	}

}
