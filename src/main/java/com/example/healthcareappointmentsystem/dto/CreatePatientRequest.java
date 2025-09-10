package com.example.healthcareappointmentsystem.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class CreatePatientRequest {
    @NotBlank(message = "Email is required")
    private String email;
    @NotBlank(message = "Password is required")
    private String password;
    @NotBlank(message = "full name is required")
    private String fullName;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private List<String> allergies;
    private List<String> chronicConditions;
}