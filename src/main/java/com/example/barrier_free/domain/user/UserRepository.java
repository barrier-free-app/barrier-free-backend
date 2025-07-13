package com.example.barrier_free.domain.user;

import com.example.barrier_free.domain.user.entity.User;
import com.example.barrier_free.domain.user.enums.SocialType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmailAndSocialType(String email, SocialType socialType);

    boolean existsByEmailAndSocialType(String email, SocialType socialType);
    boolean existsByNickname(String nickname);
    boolean existsByUsername(String username);
}
