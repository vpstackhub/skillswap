package com.skillswap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SkillswapBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(SkillswapBackendApplication.class, args);

		PasswordEncoder encoder = new BCryptPasswordEncoder(10);
System.out.println("Hashed password for admin123: " + encoder.encode("admin123"));

	}

}
