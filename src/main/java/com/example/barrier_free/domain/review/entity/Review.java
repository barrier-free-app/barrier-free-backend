package com.example.barrier_free.domain.review.entity;

import java.util.ArrayList;
import java.util.List;

import com.example.barrier_free.domain.map.entity.Map;
import com.example.barrier_free.domain.user.entity.User;
import com.example.barrier_free.global.entity.BaseEntity;

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

@Entity
public class Review extends BaseEntity {
	@OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
	private final List<ReviewImage> reviewImages = new ArrayList<>();
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "map_id")
	private Map map;
	private float rating; // 별점

	@Column(columnDefinition = "TEXT")
	private String content; // 리뷰 내용
}
