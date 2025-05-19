package com.example.barrier_free.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.barrier_free.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
