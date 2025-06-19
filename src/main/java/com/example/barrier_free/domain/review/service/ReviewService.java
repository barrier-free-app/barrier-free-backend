package com.example.barrier_free.domain.review.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.barrier_free.domain.map.repository.MapRepository;
import com.example.barrier_free.domain.report.repository.ReportRepository;
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

	private Place findPlace(Long placeId, PlaceType placeType) {
		if (placeType == PlaceType.REPORT) {
			return reportRepository.findById(placeId)
				.orElseThrow(() -> new CustomException(ErrorCode.PLACE_NOT_FOUND));
		} else {
			return mapRepository.findById(placeId)
				.orElseThrow(() -> new CustomException(ErrorCode.PLACE_NOT_FOUND));
		}
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
