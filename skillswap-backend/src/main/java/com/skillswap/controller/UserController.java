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
import com.skillswap.dto.LoginRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.skillswap.util.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = { "http://localhost:4200", "http://localhost:4201" })
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        System.out.println("📝 Received register req: " + req);

        if (req.getConfirmPassword() != null &&
            !req.getPassword().equals(req.getConfirmPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Passwords do not match");
        }

        if (userService.emailExists(req.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Email already registered");
        }

        User user = new User();
        user.setEmail(req.getEmail());
        user.setPassword(req.getPassword()); 
        user.setName(req.getName());
        
        // Enforce who can set non-STUDENT roles
        var auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth != null && auth.getAuthorities() != null &&
                auth.getAuthorities().stream().anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));

        String requestedRole = req.getRole();
        String finalRole = "STUDENT"; // default for public

        if (isAdmin && requestedRole != null && !requestedRole.isBlank()) {
            finalRole = requestedRole.toUpperCase();
        } else if (requestedRole != null && !requestedRole.equalsIgnoreCase("STUDENT")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Only ADMIN can create non-STUDENT users");
        }

        user.setRole(finalRole);

        User saved = userService.save(user); // hashes once inside service
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PostMapping(
        value = "/login",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        System.out.println("🔑 Login attempt: " + req);

        return userService.findByEmail(req.getEmail())
            .map(user -> {
                // Debug prints
                System.out.println("🔐 Raw password: " + req.getPassword());
                System.out.println("🔐 DB stored hash: " + user.getPassword());
                System.out.println("🔐 Matches? (bean) " + passwordEncoder.matches(req.getPassword(), user.getPassword()));

                // 🔍 Direct BCrypt test
                boolean test = new BCryptPasswordEncoder().matches(req.getPassword(), user.getPassword());
                System.out.println("🔍 Direct new BCrypt check: " + test);

                if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
                    System.out.println("❌ Password mismatch: raw=" + req.getPassword() +
                            " hash=" + user.getPassword());
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(new ErrorResponse("Invalid password"));
                }

                // Include role in token
                String token = jwtUtil.generateToken(user.getEmail(), user.getRole());

                return ResponseEntity.ok(new LoginResponse(token, user.getEmail(), user.getRole()));
            })
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("User not found")));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me() {
        var auth = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authenticated");
        }

        String email = String.valueOf(auth.getPrincipal()); // we set principal=email in JwtFilter
        String role = auth.getAuthorities().stream()
                .findFirst()
                .map(a -> a.getAuthority().replace("ROLE_", ""))
                .orElse("STUDENT"); // default fallback

        // Optionally load name/id from DB
        var userOpt = userService.findByEmail(email);
        String name = userOpt.map(User::getName).orElse("");
        Long id = userOpt.map(User::getId).orElse(null);

        record Me(Long id, String name, String email, String role) {}
        return ResponseEntity.ok(new Me(id, name, email, role));
    }

    // DTO for login response
    record LoginResponse(String token, String email, String role) {}

    // ✅ Inner static class for error responses
    static class ErrorResponse {
        private String error;
        public ErrorResponse(String error) { this.error = error; }
        public String getError() { return error; }
    }

    @PostConstruct
    public void init() {
        System.out.println("✅ UserController initialized!");
    }
}
