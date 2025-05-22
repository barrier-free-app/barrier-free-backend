package com.example.barrier_free.domain.report.dto;

import com.example.barrier_free.domain.report.enums.VoteType;

import lombok.Getter;

@Getter
public class VoteRequestDto {
	private Long userId;
	private VoteType voteType;
}
