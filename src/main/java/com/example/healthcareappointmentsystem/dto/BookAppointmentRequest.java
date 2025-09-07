package com.example.healthcareappointmentsystem.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BookAppointmentRequest {
    private Long doctorId;
    private Long patientId;
    private LocalDateTime startTime;
}
