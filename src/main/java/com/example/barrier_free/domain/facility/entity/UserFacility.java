package com.example.barrier_free.domain.facility.entity;

import com.example.barrier_free.domain.user.entity.User;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class UserFacility {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "facility_id")
	private Facility facility;

	public static UserFacility of(Facility facility) {
		UserFacility uf = new UserFacility();
		uf.setFacility(facility);
		return uf;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setFacility(Facility facility) {
		this.facility = facility;
	}

}
