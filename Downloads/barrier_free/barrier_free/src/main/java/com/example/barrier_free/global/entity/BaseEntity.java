package com.example.barrier_free.global.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

@Data
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class BaseEntity {
	@Column(name = "created_at", updatable = false)
	@CreatedDate
	public LocalDateTime createdAt;
	@Column(name = "updated_at")
	@LastModifiedDate
	private LocalDateTime updatedAt;
}
