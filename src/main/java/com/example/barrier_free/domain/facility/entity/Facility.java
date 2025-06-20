package com.example.barrier_free.domain.facility.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;

@Getter
@Entity
public class Facility {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	@OneToMany(mappedBy = "facility", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<MapFacility> mapFacilities = new ArrayList<>();
	@OneToMany(mappedBy = "facility", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<ReportFacility> reportFacilities = new ArrayList<>();
	@OneToMany(mappedBy = "facility", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<UserFacility> userFacilities = new ArrayList<>();

}
