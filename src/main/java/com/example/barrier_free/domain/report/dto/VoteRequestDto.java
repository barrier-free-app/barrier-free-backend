package com.example.barrier_free.domain.report.dto;

import com.example.barrier_free.domain.report.enums.VoteType;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class VoteRequestDto {
	@NotNull
	private VoteType voteType;
}
