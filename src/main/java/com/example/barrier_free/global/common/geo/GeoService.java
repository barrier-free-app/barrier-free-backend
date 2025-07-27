package com.example.barrier_free.global.common.geo;

import java.net.URI;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.barrier_free.global.exception.CustomException;
import com.example.barrier_free.global.response.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class GeoService {

	@Value("${kakao.api-key}")
	private String kakaoApiKey;

	public CoordinatesAndRegion getCoordinatesAndRegionFromAddress(String address) {

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "KakaoAK " + kakaoApiKey);
		headers.set("Accept", "application/json");
		headers.set("Content-Type", "application/json");
		HttpEntity<String> headerEntity = new HttpEntity<>(headers);

		URI targetUrl = UriComponentsBuilder
			.fromHttpUrl("https://dapi.kakao.com/v2/local/search/address")
			.queryParam("query", address)
			.build()
			.encode(StandardCharsets.UTF_8)
			.toUri();

		ResponseEntity<String> response = restTemplate.exchange(
			targetUrl, HttpMethod.GET, headerEntity, String.class);
		ObjectMapper objectMapper = new ObjectMapper();

		try {
			KakaoAddressResponse kakaoResponse = objectMapper.readValue(response.getBody(), KakaoAddressResponse.class);

			if (kakaoResponse.getDocuments().isEmpty()) {
				throw new CustomException(ErrorCode.ADDRESS_NOT_FOUND);
			}

			KakaoAddressResponse.Document doc = kakaoResponse.getDocuments().get(0);
			KakaoAddressResponse.Address addr = doc.getAddress();

			double latitude = Double.parseDouble(addr.getY());
			double longitude = Double.parseDouble(addr.getX());
			String region = addr.getDistrict();

			return new CoordinatesAndRegion(latitude, longitude, region);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CustomException(ErrorCode.JSON_PARSE_FAILED);
		}
	}

}

