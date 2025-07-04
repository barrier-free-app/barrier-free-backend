package com.example.barrier_free.global.infra;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.barrier_free.global.exception.CustomException;
import com.example.barrier_free.global.response.ErrorCode;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class S3Service {

	private final AmazonS3 amazonS3;

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	@Value("${cloud.aws.region.static}")
	private String region;

	public String uploadFile(MultipartFile multipartFile, String dirName) {

		String fileName = dirName + "/" + UUID.randomUUID() + "_" + multipartFile.getOriginalFilename();

		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(multipartFile.getSize());
		metadata.setContentType(multipartFile.getContentType());

		try (InputStream inputStream = multipartFile.getInputStream()) {
			PutObjectRequest request = new PutObjectRequest(bucket, fileName, inputStream, metadata);
			amazonS3.putObject(request);
		} catch (IOException e) {
			throw new CustomException(ErrorCode.S3_UPLOAD_FAILED);
		}

		return fileName; //url전체가 아니라 경로만 리턴함->DB에 저장하기
	}

	public void deleteFile(String fileKey) {
		try {
			amazonS3.deleteObject(bucket, fileKey);
		} catch (Exception e) {
			throw new CustomException(ErrorCode.S3_DELETE_FAILED);
		}
	}

	public String getFullUrl(String key) {
		return "https://" + bucket + ".s3." + region + ".amazonaws.com/" + key;
	}

}
