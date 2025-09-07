package com.example.healthcareappointmentsystem.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private Long userId;
    private String fullName;
    private String role;
    private String message;
    private String email;
}
