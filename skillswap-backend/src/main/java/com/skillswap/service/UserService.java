package com.skillswap.service;

import com.skillswap.model.User;
import com.skillswap.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

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
        // hash password before saving
        u.setPassword(passwordEncoder.encode(u.getPassword()));
        return repo.save(u);
    }

    public boolean emailExists(String email) {
        return repo.existsByEmail(email);
    }
}
