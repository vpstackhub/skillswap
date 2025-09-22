package com.skillswap.controller;

import com.skillswap.dto.RegisterRequest;
import com.skillswap.model.User;
import com.skillswap.service.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = { "http://localhost:4200", "http://localhost:4201" })
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        System.out.println("🔥 Received register req: " + req);

        // Optional: basic password match check
        if (req.getConfirmPassword() != null &&
            !req.getPassword().equals(req.getConfirmPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Passwords do not match");
        }

        // Optional: block duplicate emails (only if you add existsByEmail in repo)
        if (userService.emailExists(req.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Email already registered");
        }

        // Map DTO -> Entity
        User user = new User();
        user.setEmail(req.getEmail());
        user.setPassword(req.getPassword()); 

        User saved = userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PostConstruct
    public void init() {
        System.out.println("✅ UserController initialized!");
    }
}

