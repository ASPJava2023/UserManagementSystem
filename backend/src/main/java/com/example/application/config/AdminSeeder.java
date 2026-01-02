package com.example.application.config;

import com.example.application.entity.Role;
import com.example.application.entity.User;
import com.example.application.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class AdminSeeder {

    @Bean
    public CommandLineRunner seedAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            String adminEmail = "admin@example.com";
            if (!userRepository.existsByEmail(adminEmail)) {
                User admin = new User();
                admin.setName("Admin");
                admin.setEmail(adminEmail);
                admin.setPassword(passwordEncoder.encode("password"));
                admin.setRole(Role.ADMIN);
                admin.setPasswordResetRequired(false);
                userRepository.save(admin);
                System.out.println("ADMIN USER SEEDED: " + adminEmail + " / password");
            }
        };
    }
}
