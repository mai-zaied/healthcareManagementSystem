package com.example.healthcareappointmentsystem.service;

import com.example.healthcareappointmentsystem.dto.AuthResponse;
import com.example.healthcareappointmentsystem.dto.LoginRequest;
import com.example.healthcareappointmentsystem.entity.User;
import com.example.healthcareappointmentsystem.exception.ResourceNotFoundException;
import com.example.healthcareappointmentsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    public AuthResponse authenticate(LoginRequest request){
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(()-> new ResourceNotFoundException("No such Email found. Email : ", request.getEmail()));

        //will add encode when apply auth for now now autherization for easier testing
        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        AuthResponse response = new AuthResponse();
        response.setUserId(user.getId());
        response.setFullName(user.getFullName());
        response.setRole(user.getRole().name());
        response.setEmail(user.getEmail());
        response.setMessage("Login successful");

        return response;
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
    }
}
