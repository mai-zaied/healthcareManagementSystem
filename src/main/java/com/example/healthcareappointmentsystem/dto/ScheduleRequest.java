package com.example.healthcareappointmentsystem.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ScheduleRequest {
    private Long doctorId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer slotDuration = 30;
}
