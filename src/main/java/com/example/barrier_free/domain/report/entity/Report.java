package com.example.barrier_free.domain.report.entity;

import java.util.ArrayList;
import java.util.List;

import com.example.barrier_free.domain.comment.entity.Comment;
import com.example.barrier_free.domain.facility.entity.ReportFacility;
import com.example.barrier_free.domain.user.entity.User;
import com.example.barrier_free.global.entity.PlaceEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Report extends PlaceEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ReportFacility> reportFacilities = new ArrayList<>();
	@OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Comment> comments = new ArrayList<>();
	@OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ReportImage> reportImages = new ArrayList<>();
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

}
