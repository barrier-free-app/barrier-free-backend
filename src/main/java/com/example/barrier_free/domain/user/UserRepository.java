package com.example.barrier_free.domain.user;

import com.example.barrier_free.domain.user.entity.User;
import com.example.barrier_free.domain.user.enums.SocialType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    boolean existsByEmailAndSocialType(String email, SocialType socialType);
}
