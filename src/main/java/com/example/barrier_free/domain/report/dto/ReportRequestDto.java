package com.example.barrier_free.domain.report.dto;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReportRequestDto {
	private String description;
	@NotNull
	private String name;
	@NotNull
	private String address;
	@NotNull
	private int imageType;
	@NotNull
	private List<Integer> facilities;

	private String homepage;
	private String openingHours;
	private String contact;

}
