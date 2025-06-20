package com.example.barrier_free.domain.report.dto;

import java.util.List;

import lombok.Data;

@Data
public class ReportRequestDto {
	/*추후 토큰 구현되면 토큰에서 유저 뽑아서 쓰면 되므로 이곳은 삭제 됩니다*/
	private Long userId;
	/*삭제 예정*/

	private String description;
	private String name;
	private String address;
	private List<Integer> facilities;
	private String phone;
	private String hours;

}
