package com.example.healthcareappointmentsystem.controller;

import com.example.healthcareappointmentsystem.collection.Prescription;
import com.example.healthcareappointmentsystem.dto.AppointmentResponse;
import com.example.healthcareappointmentsystem.dto.CreatePrescriptionRequest;
import com.example.healthcareappointmentsystem.dto.ScheduleRequest;
import com.example.healthcareappointmentsystem.dto.ScheduleResponse;
import com.example.healthcareappointmentsystem.entity.Appointment;
import com.example.healthcareappointmentsystem.entity.DoctorSchedule;
import com.example.healthcareappointmentsystem.service.AppointmentService;
import com.example.healthcareappointmentsystem.service.DoctorScheduleService;
import com.example.healthcareappointmentsystem.service.DoctorService;
import com.example.healthcareappointmentsystem.service.PrescriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctor")
@RequiredArgsConstructor
public class DoctorController {
    private final AppointmentService appointmentService;
    private final PrescriptionService prescriptionService;
    private final DoctorScheduleService doctorScheduleService;
    private final DoctorService doctorService;

    // Appointment Management
    @GetMapping("/appointments")
    public ResponseEntity<List<AppointmentResponse>> getDoctorAppointments() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Long doctorId = doctorService.findByEmail(email).getId();
        List<AppointmentResponse> appointments = appointmentService.getDoctorAppointments(doctorId);
        return ResponseEntity.ok(appointments);
    }

    @PatchMapping("/appointments/{id}/complete")
    public ResponseEntity<AppointmentResponse> markAppointmentCompleted(@PathVariable Long id) {
        AppointmentResponse appointment = appointmentService.markAppointmentCompleted(id);
        return ResponseEntity.ok(appointment);
    }


    // Prescription Management
    @PostMapping("/prescriptions")
    public ResponseEntity<Prescription> createPrescription(@Valid @RequestBody CreatePrescriptionRequest request) {
        Prescription prescription = prescriptionService.createPrescription(request);
        return ResponseEntity.ok(prescription);
    }

    @GetMapping("/prescriptions")
    public ResponseEntity<List<Prescription>> getDoctorPrescriptions() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Long doctorId = doctorService.findByEmail(email).getId();
        List<Prescription> prescriptions = prescriptionService.getDoctorPrescriptions(doctorId);
        return ResponseEntity.ok(prescriptions);
    }

    // Schedule Management
    @PostMapping("/schedule")
    public ResponseEntity<ScheduleResponse> addAvailability(@Valid @RequestBody ScheduleRequest scheduleRequest) {
        ScheduleResponse savedSchedule = doctorScheduleService.addDoctorSchedule(scheduleRequest);
        return ResponseEntity.ok(savedSchedule);
    }

    @GetMapping("/schedule")
    public ResponseEntity<List<ScheduleResponse>> getDoctorSchedule() {
        List<ScheduleResponse> schedule = doctorScheduleService.getDoctorSchedule();
        return ResponseEntity.ok(schedule);
    }
}