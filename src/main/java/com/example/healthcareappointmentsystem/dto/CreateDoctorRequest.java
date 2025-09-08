package com.example.healthcareappointmentsystem.dto;

import lombok.Data;

@Data
public class CreateDoctorRequest {
    private String email;
    private String password;
    private String fullName;
    private String specialty;
    private String phoneNumber;
}