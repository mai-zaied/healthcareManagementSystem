package com.example.healthcareappointmentsystem.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class CreatePatientRequest {
    private String email;
    private String password;
    private String fullName;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private List<String> allergies;
    private List<String> chronicConditions;
}