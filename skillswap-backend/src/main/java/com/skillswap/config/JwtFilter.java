package com.skillswap.config;

import com.skillswap.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.io.IOException;
import org.springframework.lang.NonNull;



@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;  // Injected instance

    @Override
    protected void doFilterInternal(
             @NonNull HttpServletRequest request,
             @NonNull HttpServletResponse response,
             @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // Skip CORS preflight requests
    if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
        response.setStatus(HttpServletResponse.SC_OK);
        return;
    }


        // 1) Skip public endpoints
        String path = request.getServletPath();
            if (path.startsWith("/api/users/login")
            || path.startsWith("/h2-console")) {
        filterChain.doFilter(request, response);
        return;
    }


        // 2) Read Authorization header
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3) Parse token and extract subject + role
        String token = header.substring(7);
        String email;
        try {
            email = jwtUtil.validateToken(token);   // sub (email)
        } catch (Exception e) {
            filterChain.doFilter(request, response);
            return;
        }

        String role = null;
        try {
            role = jwtUtil.getRoleFromToken(token); // "STUDENT" | "TEACHER" | "ADMIN"
        } catch (Exception ignored) {}

        // 4) Attach authentication with authorities
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            var authorities = (role != null && !role.isBlank())
                    ? java.util.List.of(new SimpleGrantedAuthority(role))
                    : java.util.List.<SimpleGrantedAuthority>of();
                    
                    System.out.println("🧩 JWT email=" + email + " role=" + role);

            var auth = new UsernamePasswordAuthenticationToken(email, null, authorities);
            System.out.println("🛡️ Authorities to set: " + authorities);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        // 5) Continue chain
        filterChain.doFilter(request, response);
    }
}