package com.example.healthcareappointmentsystem.controller;

import com.example.healthcareappointmentsystem.repository.UserRepository;
import com.example.healthcareappointmentsystem.security.Role;
import com.example.healthcareappointmentsystem.service.UserRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final UserRegistrationService userRegistrationService;
    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        boolean adminExists = userRepository.findByRole(Role.ADMIN).isPresent();

        if (!adminExists) {
            String adminEmail = "admin@hospital.com";
            String adminPassword = "admin123";
            String adminName = "Admin User";

            userRegistrationService.saveAdmin(adminEmail, adminPassword, adminName);
            System.out.println("Admin created successfully!");
        } else {
            System.out.println("Admin already exists. Skipping creation.");
        }
    }
}

