package com.example.barrier_free.domain.report.dto;

import java.util.List;

import lombok.Data;

@Data
public class ReportRequestDto {
	private String name;
	private String address;
	private String description;
	private int imageType;
	private List<Integer> facilities;
	private String homepage;
	private String openingHours;
	private String contact;

}
