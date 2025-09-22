package com.skillswap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.skillswap.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
}


