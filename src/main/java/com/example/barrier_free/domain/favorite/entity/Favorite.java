package com.example.barrier_free.domain.favorite.entity;

import com.example.barrier_free.domain.map.entity.Map;
import com.example.barrier_free.domain.report.entity.Report;
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

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Favorite {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "report_id")
	private Report report;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "map_id")
	private Map map;

	public static Favorite fromMap(User user, Map map) {
		Favorite favorite = new Favorite();
		favorite.user = user;
		favorite.map = map;
		return favorite;
	}

	public static Favorite fromReport(User user, Report report) {
		Favorite favorite = new Favorite();
		favorite.user = user;
		favorite.report = report;
		return favorite;
	}

}
