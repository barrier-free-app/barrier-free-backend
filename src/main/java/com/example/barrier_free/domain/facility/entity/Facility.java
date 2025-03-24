package com.example.barrier_free.domain.facility.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Facility {
	@OneToMany(mappedBy = "facility", fetch = FetchType.LAZY)
	private final List<MapFacility> mapFacilities = new ArrayList<>();
	@OneToMany(mappedBy = "facility", fetch = FetchType.LAZY)
	private final List<ReportFacility> reportFacilities = new ArrayList<>();
	@OneToMany(mappedBy = "facility", fetch = FetchType.LAZY)
	private final List<UserFacility> userFacilities = new ArrayList<>();
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;

}
