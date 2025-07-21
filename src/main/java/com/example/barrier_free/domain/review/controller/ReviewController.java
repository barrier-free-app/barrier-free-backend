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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "리뷰", description = "리뷰 작성, 조회, 삭제 관련 API")
@RequiredArgsConstructor
@RestController
public class ReviewController {
	private final ReviewService reviewService;

	@Operation(
		summary = "장소 리뷰 조회 API",
		description = """
			페이징 형식입니다.
			기본 정렬은 생성일 내림차순
			- page (기본값: 0): 페이지 번호 (0부터 시작)
			- size (기본값: 10): 한 페이지당 항목 수
			- sort 정렬 기준 
						
			응답값
			- hasNext: 다음 페이지 존재여부
			- totalPages: 전체 페이지 수
			""")
	@GetMapping(value = "places/{placeId}/reviews")
	public ApiResponse<?> getReviews(
		@PathVariable long placeId,
		@RequestParam PlaceType type,
		@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
		PlaceReviewPageResponse reviews = reviewService.getReviewsByPlace(placeId, type, pageable);
		return ApiResponse.success(SuccessCode.OK, reviews);
	}

	@Operation(
		summary = "장소 리뷰 작성 API")
	@PostMapping(value = "places/{placeId}/reviews", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ApiResponse<?> createReview(@RequestPart("review") ReviewRequestDto requestDto,
		@RequestPart(value = "image", required = false) List<MultipartFile> images,
		@PathVariable long placeId,
		@RequestParam PlaceType type
	) {
		Long reviewId = reviewService.createReview(placeId, requestDto, images, type);
		return ApiResponse.success(SuccessCode.REVIEW_CREATED, Map.of("reviewId", reviewId));
	}

	@Operation(
		summary = "유저의 리뷰 삭제 API")
	@DeleteMapping(value = "users/reviews/{reviewId}")
	public ApiResponse<?> deleteReview(
		@PathVariable long reviewId
	) {
		reviewService.deleteReview(reviewId);
		return ApiResponse.success(SuccessCode.OK, "삭제완료");
	}

	@Operation(
		summary = "유저의 리뷰 조회 API",
		description = """
			페이징 형식입니다.
			기본 정렬은 생성일 내림차순
			- page (기본값: 0): 페이지 번호 (0부터 시작)
			- size (기본값: 10): 한 페이지당 항목 수
			- sort 정렬 기준			
			응답값
			- hasNext: 다음 페이지 존재여부
			- totalPages: 전체 페이지 수
			""")
	@GetMapping(value = "users/reviews")
	public ApiResponse<?> getUserReviews(
		@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
	) {
		UserReviewPageResponse reviews = reviewService.getReviewsByUser(pageable);
		return ApiResponse.success(SuccessCode.OK, reviews);
	}

}
