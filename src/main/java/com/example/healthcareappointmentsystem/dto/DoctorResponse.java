package com.example.healthcareappointmentsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DoctorResponse {
    private Long id;
    private String email;
    private String fullName;
    private String role;
    private String specialty;
    private String phoneNumber;
}
