package com.example.barrier_free.domain.report.dto;

import com.example.barrier_free.domain.report.enums.VoteStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class VoteInfoResponse {
	private int up;
	private int down;
	private VoteStatus userVote;
}
