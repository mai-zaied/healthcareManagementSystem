package com.example.healthcareappointmentsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ScheduleResponse {
    private String date;
    private String startTime;
    private String endTime;
    private Integer slotDuration;
}
