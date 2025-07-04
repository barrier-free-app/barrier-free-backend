package com.example.barrier_free.global.jwt;

import org.springframework.security.core.context.SecurityContextHolder;

public class JwtUserUtils {

    public static Long getCurrentUserId() {
        return (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
