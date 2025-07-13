package com.example.barrier_free.domain.user.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.barrier_free.domain.facility.entity.Facility;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import com.example.barrier_free.domain.facility.entity.UserFacility;
import com.example.barrier_free.domain.favorite.entity.Favorite;
import com.example.barrier_free.domain.report.entity.Report;
import com.example.barrier_free.domain.report.entity.Vote;
import com.example.barrier_free.domain.user.enums.Role;
import com.example.barrier_free.domain.user.enums.SocialType;
import com.example.barrier_free.domain.user.enums.UserType;
import com.example.barrier_free.global.common.BaseEntity;

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
@Getter
@NoArgsConstructor
public class User extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Email
	private String email; 	 // 이메일, 겹칠 수 있음

	@Column(unique = true)
	private String username; // 아이디, 겹치면 안 됨

	private String password;

	@Column(unique = true, length = 30)
	private String nickname; // 닉네임, 겹치면 안 됨

	// TODO: 토큰 삭제
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
	private List<Vote> votes = new ArrayList<>();

	// 생성자
	// 일반 로그인
	public User(String email, String nickname, String username,
				String password, UserType userType) {
		this.email = email;
		this.nickname = nickname;
		this.username = username;
		this.password = password;
		this.userType = userType;

		this.incorrectTimes = 0;	// 실패 횟수 초기화
		this.socialType = SocialType.GENERAL;
	}

	// 소셜 로그인
	public User(String email, String nickname, SocialType socialType) {
		this.email = email;
		this.nickname = nickname;
		this.socialType = socialType;
	}

	public void setTokens(String accessToken, String refreshToken) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}

	// 사용자 편의시설 단일 추가 (중복 방지)
	public void setUserFacility(Facility facility) {
		boolean alreadyExists = this.userFacilities.stream()
				.anyMatch(uf -> uf.getFacility().getId().equals(facility.getId()));

		if (!alreadyExists) {
			UserFacility uf = new UserFacility(this, facility);  // 연관관계 직접 설정
			this.userFacilities.add(uf);
		}
	}

	// 여러 사용자 편의시설 한번에 추가 (중복 방지 포함)
	public void setUserFacilities(List<Facility> facilities) {
		this.userFacilities.clear();
		facilities.forEach(this::setUserFacility);
	}

	// 비밀번호 틀린 횟수
	public void setIncorrectTimes() {
		this.incorrectTimes++;
	}

	// 비밀번호 실패 횟수 초기화
	public void resetIncorrectTimes() {
		this.incorrectTimes = 0;
	}
}
