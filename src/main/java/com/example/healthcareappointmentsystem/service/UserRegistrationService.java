package com.example.healthcareappointmentsystem.service;

import com.example.healthcareappointmentsystem.entity.User;
import com.example.healthcareappointmentsystem.repository.UserRepository;
import com.example.healthcareappointmentsystem.security.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User saveAdmin(String email, String rawPassword, String fullName) {
        User admin = new User();
        admin.setEmail(email);
        admin.setPassword(passwordEncoder.encode(rawPassword));
        admin.setFullName(fullName);
        admin.setRole(Role.ADMIN);
        return userRepository.save(admin);
    }
}
