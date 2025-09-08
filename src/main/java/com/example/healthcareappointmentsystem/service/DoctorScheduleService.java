package com.example.healthcareappointmentsystem.service;

import com.example.healthcareappointmentsystem.dto.ScheduleRequest;
import com.example.healthcareappointmentsystem.entity.Doctor;
import com.example.healthcareappointmentsystem.entity.DoctorSchedule;
import com.example.healthcareappointmentsystem.exception.ResourceNotFoundException;
import com.example.healthcareappointmentsystem.repository.DoctorRepository;
import com.example.healthcareappointmentsystem.repository.DoctorScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorScheduleService {
    private final DoctorScheduleRepository doctorScheduleRepository;
    private final DoctorRepository doctorRepository;

    public DoctorSchedule addDoctorSchedule(ScheduleRequest scheduleRequest){
        Doctor doctor = doctorRepository.findById(scheduleRequest.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", scheduleRequest.getDoctorId()));
        DoctorSchedule schedule = new DoctorSchedule();
        schedule.setDoctor(doctor);
        schedule.setDate(scheduleRequest.getDate());
        schedule.setStartTime(scheduleRequest.getStartTime());
        schedule.setEndTime(scheduleRequest.getEndTime());
        schedule.setSlotDuration(scheduleRequest.getSlotDuration());
        return doctorScheduleRepository.save(schedule);
    }

    public List<DoctorSchedule> getDoctorSchedule(Long doctorId) {
        return doctorScheduleRepository.findByDoctorId(doctorId);
    }

    public void removeDoctorSchedule(Long scheduleId) {
        if (!doctorScheduleRepository.existsById(scheduleId)) {
            throw new ResourceNotFoundException("Doctor schedule", scheduleId);
        }
        doctorScheduleRepository.deleteById(scheduleId);
    }
}