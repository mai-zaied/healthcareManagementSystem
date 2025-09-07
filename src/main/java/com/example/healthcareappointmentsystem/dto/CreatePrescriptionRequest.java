package com.example.healthcareappointmentsystem.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CreatePrescriptionRequest {
    private Long patientId;
    private Long doctorId;
    private Long appointmentId;
    private String diagnosis;
    private String notes;
    private List<String> medicines;
}

