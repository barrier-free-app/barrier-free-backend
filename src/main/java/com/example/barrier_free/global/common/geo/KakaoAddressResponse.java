package com.example.barrier_free.global.common.geo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class KakaoAddressResponse {
	public List<Document> documents;

	@JsonIgnoreProperties(ignoreUnknown = true)
	@Getter
	public static class Document {
		private Address address;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	@Getter
	public static class Address {
		private String x; // 경도
		private String y; // 위도
		@JsonProperty("region_2depth_name")
		private String district;
	}
}
