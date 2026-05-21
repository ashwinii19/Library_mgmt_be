package com.libr.mng.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.libr.mng.entity.User;
import com.libr.mng.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        String adminEmail = "admin@library.com";

        String adminPassword = "admin123";

        if (!userRepository.findByEmail(adminEmail).isPresent()) {

            User admin = new User();

            admin.setName("Super Admin");

			admin.setEmail(adminEmail);

            admin.setPassword(
                    passwordEncoder.encode(adminPassword)
            );

            admin.setRole("ADMIN");

            admin.setEmployeeId(1L);

            userRepository.save(admin);

            System.out.println(
                    "======================================================"
            );

            System.out.println(
                    "Default Admin user created successfully!"
            );

            System.out.println(
                    "Email: " + adminEmail
            );

            System.out.println(
                    "Password: " + adminPassword
            );

            System.out.println(
                    "======================================================"
            );

        } else {

            System.out.println(
                    "Default admin already exists. Skipping creation."
            );
        }
    }
}