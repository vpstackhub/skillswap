package com.skillswap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.skillswap.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Custom finder for login
    Optional<User> findByEmail(String email);

    // Optional: used for registration duplicate check
    boolean existsByEmail(String email);
}
