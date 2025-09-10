package com.example.healthcareappointmentsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponse {
    private Long appointmentId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private Long doctorId;
    private String doctorFullName;
    private String doctorSpecialty;
    private String patientFullName;
}
