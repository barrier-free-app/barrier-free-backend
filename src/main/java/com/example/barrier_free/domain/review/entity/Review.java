package com.example.barrier_free.domain.review.entity;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.BatchSize;

import com.example.barrier_free.domain.map.entity.Map;
import com.example.barrier_free.domain.report.entity.Report;
import com.example.barrier_free.domain.user.entity.User;
import com.example.barrier_free.global.common.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Review extends BaseEntity {
	@Getter
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "map_id")
	private Map map;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "report_id")
	private Report report;
	@OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
	@BatchSize(size = 10)
	private List<ReviewImage> reviewImages = new ArrayList<>();
	private float rating; // 별점
	@Column(columnDefinition = "TEXT")
	private String content; // 리뷰 내용

	@Builder
	public Review(User user, Map map, Report report, float rating, String content) {
		this.user = user;
		this.map = map;
		this.report = report;
		this.rating = rating;
		this.content = content;
		this.reviewImages = new ArrayList<>();
	}

	public void addImage(ReviewImage image) {
		reviewImages.add(image);         // 리뷰에 이미지 추가
		image.setReview(this);     // 이미지에 리뷰 설정
	}

	public void attachMap(Map map) {
		this.map = map;
	}

	public void attachReport(Report report) {
		this.report = report;
	}

}
