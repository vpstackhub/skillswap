package com.skillswap.controller;

import com.skillswap.model.User;
import com.skillswap.repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.List;

@RestController
@RequestMapping("/api/teacher")
@CrossOrigin(origins = { "http://localhost:4200", "http://localhost:4201" })
public class TeacherController {

    private final UserRepository userRepository;
    

    private final PasswordEncoder passwordEncoder;

    public TeacherController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;

    }

    @GetMapping("/ping")
    public String ping() {
        return "👨‍🏫 teacher pong";
    }

    // ✅ Fetch all teachers (final version)
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllTeachers() {
        List<User> teachers = userRepository.findAll()
            .stream()
            .filter(u -> "TEACHER".equalsIgnoreCase(u.getRole()))
            .toList();

        return ResponseEntity.ok(teachers);
    }


    // ✅ Fetch teacher by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getTeacherById(@PathVariable Long id) {
        return userRepository.findById(id)
                .filter(user -> "TEACHER".equalsIgnoreCase(user.getRole()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

      @PostMapping("/create")
    public ResponseEntity<User> createTeacher(@RequestBody User teacher) {
        teacher.setRole("TEACHER");
        teacher.setPassword(passwordEncoder.encode(teacher.getPassword()));
        User saved = userRepository.save(teacher);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}