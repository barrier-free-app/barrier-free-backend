package com.example.barrier_free.domain.review.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.barrier_free.domain.map.repository.MapRepository;
import com.example.barrier_free.domain.report.repository.ReportRepository;
import com.example.barrier_free.domain.review.dto.ReviewPageResponse;
import com.example.barrier_free.domain.review.dto.ReviewRequestDto;
import com.example.barrier_free.domain.review.entity.Review;
import com.example.barrier_free.domain.review.entity.ReviewImage;
import com.example.barrier_free.domain.review.repository.ReviewRepository;
import com.example.barrier_free.domain.user.UserRepository;
import com.example.barrier_free.domain.user.entity.User;
import com.example.barrier_free.global.common.Place;
import com.example.barrier_free.global.common.PlaceType;
import com.example.barrier_free.global.exception.CustomException;
import com.example.barrier_free.global.infra.S3Service;
import com.example.barrier_free.global.response.ErrorCode;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ReviewService {
	private final ReviewRepository reviewRepository;
	private final S3Service s3Service;
	private final MapRepository mapRepository;
	private final UserRepository userRepository;
	private final ReportRepository reportRepository;
	//1.리뷰 조회 getReviewsByPlace 페이징 받기

	public ReviewPageResponse getReviewsByPlace(Long placeId, PlaceType placeType, Pageable pageable) {
		//1. 장소찾기
		Place place = findPlace(placeId, placeType);
		//2.리뷰 찾기
		Page<Review> reviews = getReviewsFromPlace(place, pageable);
		return ReviewPageResponse.from(reviews, s3Service);
	}

	public ReviewPageResponse getReviewsByUser(Long userId) {
		//유저 존재 확인
		User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
		//리뷰 찾기
		//facilities 를 필터링에 이용
		//이거 할때 userId->리뷰 찾기
		//리뷰->map/report 장소찾기
		// in facilities 해당하는 애들 찾기
		//걔네의 placeId와 type 가지고 오기
		//걔네만 response로 변경해서 return 하기

		List<Review> reviews = reviewRepository.findByUserId(userId);

		//리뷰 Id, PlaceId(map또는 report겟다), 사진, content, rating 전달
		return UserReviewResponse.from(reviews, s3Service);

	}

	@Transactional
	public Long createReview(Long placeId, ReviewRequestDto dto, List<MultipartFile> images, PlaceType placeType) {
		User user = userRepository.findById(dto.getUserId())
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

		Review review = Review.builder()
			.user(user)
			.content(dto.getContent())
			.rating(dto.getRating())
			.build();

		Place place = findPlace(placeId, placeType);
		place.attachTo(review);
		List<String> uploadedKeys = new ArrayList<>();

		try {
			uploadedKeys = uploadAndAttachReviewImages(review, images);
			reviewRepository.save(review);
			return review.getId();

		} catch (Exception e) {
			// 실패 시 S3에 업로드한 이미지 삭제
			for (String key : uploadedKeys) {
				s3Service.deleteFile(key);
			}
			throw e;
		}
	}

	@Transactional
	public void deleteReview(long userId, long reviewId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

		Review review = reviewRepository.findById(reviewId)
			.orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));
		Long writer = review.getUser().getId();
		if (!writer.equals(user.getId())) {
			throw new CustomException(ErrorCode.USER_NOT_AUTHORIZED);
		}

		review.getReviewImages().forEach(image -> {
			s3Service.deleteFile(image.getUrl()); // 이미지 URL이 S3 key면 OK
		});

		reviewRepository.delete(review);
	}

	private Place findPlace(Long placeId, PlaceType placeType) {
		if (placeType == PlaceType.report) {
			return reportRepository.findById(placeId)
				.orElseThrow(() -> new CustomException(ErrorCode.PLACE_NOT_FOUND));
		} else {
			return mapRepository.findById(placeId)
				.orElseThrow(() -> new CustomException(ErrorCode.PLACE_NOT_FOUND));
		}
	}

	private Page<Review> getReviewsFromPlace(Place place, Pageable pageable) {
		Page<Review> reviews;

		if (place.getPlaceType() == PlaceType.report) {
			reviews = reviewRepository.findByReportId(place.getId(), pageable);
		} else {
			reviews = reviewRepository.findByMapId(place.getId(), pageable);
		}

		return reviews;
	}

	private List<String> uploadAndAttachReviewImages(Review review, List<MultipartFile> images) {
		List<String> uploadedKeys = new ArrayList<>();

		if (images == null || images.isEmpty())
			return uploadedKeys;
		for (MultipartFile image : images) {
			if (!image.isEmpty()) {
				String key = s3Service.uploadFile(image, "review-images");
				review.addImage(new ReviewImage(key));
				uploadedKeys.add(key);
			}
		}
		return uploadedKeys;

	}

}
