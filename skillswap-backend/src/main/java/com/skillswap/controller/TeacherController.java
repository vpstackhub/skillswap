package com.skillswap.controller;

import com.skillswap.model.User;
import com.skillswap.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teacher")
public class TeacherController {

    private final UserRepository userRepository;

    public TeacherController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/ping")
    public String ping() {
        return "👨‍🏫 teacher pong";
    }

    // New endpoint: fetch teacher info by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getTeacherById(@PathVariable Long id) {
        return userRepository.findById(id)
                .filter(user -> "TEACHER".equalsIgnoreCase(user.getRole()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
