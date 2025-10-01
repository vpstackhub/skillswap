package com.skillswap.service;

import com.skillswap.model.User;
import com.skillswap.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return repo.findAll();
    }

    public User save(User u) {
        u.setPassword(passwordEncoder.encode(u.getPassword())); // hash password
        return repo.save(u);
    }

    public boolean emailExists(String email) {
        return repo.existsByEmail(email);
    }

    // ✅ Add this method to support login lookups
    public Optional<User> findByEmail(String email) {
        return repo.findByEmail(email);
    }
}
