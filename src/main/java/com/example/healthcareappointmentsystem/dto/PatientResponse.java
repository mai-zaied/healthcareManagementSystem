package com.example.healthcareappointmentsystem.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class PatientResponse {
    private Long id;
    private String email;
    private String fullName;
    private String role;
    private String phoneNumber;
    private LocalDate dateOfBirth;
}