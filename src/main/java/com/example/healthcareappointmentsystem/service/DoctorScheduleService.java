package com.example.healthcareappointmentsystem.service;
import com.example.healthcareappointmentsystem.dto.ScheduleRequest;
import com.example.healthcareappointmentsystem.dto.ScheduleResponse;
import com.example.healthcareappointmentsystem.entity.Doctor;
import com.example.healthcareappointmentsystem.entity.DoctorSchedule;
import com.example.healthcareappointmentsystem.exception.ResourceNotFoundException;
import com.example.healthcareappointmentsystem.repository.DoctorRepository;
import com.example.healthcareappointmentsystem.repository.DoctorScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
/**
 * Service layer for managing Doctor Schedule between patients and doctors.
 */
@Service
@RequiredArgsConstructor
public class DoctorScheduleService {
    private final DoctorScheduleRepository doctorScheduleRepository;
    private final DoctorRepository doctorRepository;
    /**
     * Adds a new schedule for the currently authenticated doctor.
     * @param scheduleRequest the request object containing:
     *                        - date of schedule
     *                        - start and end times
     * @return a ScheduleResponse DTO containing the date, start time, end time, and slot duration.
     */
    public ScheduleResponse addDoctorSchedule(ScheduleRequest scheduleRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Doctor doctor = doctorRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", email));
        LocalDate today = LocalDate.now();
        LocalTime nowTime = LocalTime.now();
        if (scheduleRequest.getDate().isBefore(today)) {
            throw new IllegalArgumentException("Schedule date must be in the future.");
        }
        if (scheduleRequest.getDate().isEqual(today) &&
                scheduleRequest.getStartTime().isBefore(nowTime)) {
            throw new IllegalArgumentException("Schedule start time must be in the future.");
        }
        List<DoctorSchedule> existingSchedules =
                doctorScheduleRepository.findByDoctorId(doctor.getId());
        for (DoctorSchedule existing : existingSchedules) {
            if (existing.getDate().isEqual(scheduleRequest.getDate())) {
                boolean overlaps =
                        !(scheduleRequest.getEndTime().isBefore(existing.getStartTime()) ||
                                scheduleRequest.getStartTime().isAfter(existing.getEndTime()));
                if (overlaps) {
                    throw new IllegalArgumentException("Schedule overlaps with an existing schedule.");
                }
            }
        }
        DoctorSchedule schedule = new DoctorSchedule();
        schedule.setDoctor(doctor);
        schedule.setDate(scheduleRequest.getDate());
        schedule.setStartTime(scheduleRequest.getStartTime());
        schedule.setEndTime(scheduleRequest.getEndTime());
        schedule.setSlotDuration(scheduleRequest.getSlotDuration());
        DoctorSchedule savedSchedule = doctorScheduleRepository.save(schedule);
        return new ScheduleResponse(
                savedSchedule.getDate().toString(),
                savedSchedule.getStartTime().toString(),
                savedSchedule.getEndTime().toString(),
                savedSchedule.getSlotDuration()
        );
    }
    public List<ScheduleResponse> getDoctorSchedule() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Doctor doctor = doctorRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", email));
        return doctorScheduleRepository.findByDoctorId(doctor.getId())
                .stream()
                .map(schedule -> new ScheduleResponse(
                        schedule.getDate().toString(),
                        schedule.getStartTime().toString(),
                        schedule.getEndTime().toString(),
                        schedule.getSlotDuration()
                ))
                .toList();
    }
}