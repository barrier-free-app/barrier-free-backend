package com.example.barrier_free.domain.report.entity;

import java.util.ArrayList;
import java.util.List;

import com.example.barrier_free.domain.facility.entity.ReportFacility;
import com.example.barrier_free.domain.favorite.entity.Favorite;
import com.example.barrier_free.domain.favorite.entity.WeeklyRank;
import com.example.barrier_free.domain.place.entity.PlaceEntity;
import com.example.barrier_free.domain.place.enums.PlaceType;
import com.example.barrier_free.domain.report.enums.VoteType;
import com.example.barrier_free.domain.review.entity.Review;
import com.example.barrier_free.domain.user.entity.User;
import com.example.barrier_free.global.common.Place;

import jakarta.persistence.CascadeType;
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
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Report extends PlaceEntity implements Place {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Builder.Default
	@OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ReportFacility> reportFacilities = new ArrayList<>();
	@Builder.Default
	@OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Vote> votes = new ArrayList<>();
	@Builder.Default
	@OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Favorite> favorites = new ArrayList<>();
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	@Builder.Default
	@OneToMany(mappedBy = "report", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<WeeklyRank> weeklyRanks = new ArrayList<>();

	public void addReportFacility(ReportFacility reportFacility) {
		reportFacilities.add(reportFacility);
		reportFacility.setReport(this);
	}

	public Vote createVote(VoteType voteType, User user) {
		Vote vote = new Vote(voteType, user, this);
		votes.add(vote);
		return vote;
	}

	@Override
	public void attachTo(Review review) {
		review.attachReport(this); // 리뷰에 맵 연결
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public PlaceType getPlaceType() {
		return PlaceType.report;
	}

	@Override
	public List<Integer> getFacility() {
		return reportFacilities.stream()
			.map(mapFacility -> mapFacility.getFacility().getId())
			.toList();
	}

}
