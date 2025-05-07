package com.example.barrier_free.domain.user.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;

import com.example.barrier_free.domain.facility.entity.UserFacility;
import com.example.barrier_free.domain.favorite.entity.Favorite;
import com.example.barrier_free.domain.report.entity.Report;
import com.example.barrier_free.domain.report.entity.Vote;
import com.example.barrier_free.domain.user.enums.Role;
import com.example.barrier_free.domain.user.enums.SocialType;
import com.example.barrier_free.domain.user.enums.UserType;
import com.example.barrier_free.global.entity.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;

@Entity
public class User extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Email
	@Column(unique = true)
	private String email;
	private String password;
	@Column(length = 30)
	private String nickname;
	private String accessToken;
	private String refreshToken;

	@Enumerated(EnumType.STRING)
	private UserType userType;
	@Enumerated(EnumType.STRING)
	private SocialType socialType;
	@Enumerated(EnumType.STRING)
	private Role role;

	@Column(name = "incorrect_times")
	private int incorrectTimes;

	@CreatedDate
	@Column(name = "nickname_updated_at")
	private LocalDateTime nicknameUpdatedAt;
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<UserFacility> userFacilities = new ArrayList<>();
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Favorite> favorites = new ArrayList<>();
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Report> reports = new ArrayList<>();
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Vote> votess = new ArrayList<>();

}
