package com.example.healthcareappointmentsystem.controller;

import com.example.healthcareappointmentsystem.collection.Prescription;
import com.example.healthcareappointmentsystem.dto.CreatePrescriptionRequest;
import com.example.healthcareappointmentsystem.dto.ScheduleRequest;
import com.example.healthcareappointmentsystem.entity.Appointment;
import com.example.healthcareappointmentsystem.entity.DoctorSchedule;
import com.example.healthcareappointmentsystem.service.AppointmentService;
import com.example.healthcareappointmentsystem.service.DoctorScheduleService;
import com.example.healthcareappointmentsystem.service.PrescriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctor")
@RequiredArgsConstructor
public class DoctorController {
    private final AppointmentService appointmentService;
    private final PrescriptionService prescriptionService;
    private final DoctorScheduleService doctorScheduleService;

    // Appointment Management
    @GetMapping("/appointments")
    public ResponseEntity<List<Appointment>> getDoctorAppointments(@RequestParam Long doctorId) {
        List<Appointment> appointments = appointmentService.getDoctorAppointments(doctorId);
        return ResponseEntity.ok(appointments);
    }

    @PatchMapping("/appointments/{id}/complete")
    public ResponseEntity<Appointment> markAppointmentCompleted(@PathVariable Long id) {
        Appointment appointment = appointmentService.markAppointmentCompleted(id);
        return ResponseEntity.ok(appointment);
    }

    // Prescription Management
    @PostMapping("/prescriptions")
    public ResponseEntity<Prescription> createPrescription(@Valid @RequestBody CreatePrescriptionRequest request) {
        Prescription prescription = prescriptionService.createPrescription(request);
        return ResponseEntity.ok(prescription);
    }

    @GetMapping("/prescriptions")
    public ResponseEntity<List<Prescription>> getDoctorPrescriptions(@RequestParam Long doctorId) {
        List<Prescription> prescriptions = prescriptionService.getDoctorPrescriptions(doctorId);
        return ResponseEntity.ok(prescriptions);
    }

    // Schedule Management
    @PostMapping("/schedule")
    public ResponseEntity<DoctorSchedule> addAvailability(@Valid @RequestBody ScheduleRequest scheduleRequest) {
        DoctorSchedule savedSchedule = doctorScheduleService.addDoctorSchedule(scheduleRequest);
        return ResponseEntity.ok(savedSchedule);
    }
    @GetMapping("/schedule")
    public ResponseEntity<List<DoctorSchedule>> getDoctorSchedule(@RequestParam Long doctorId) {
        List<DoctorSchedule> schedule = doctorScheduleService.getDoctorSchedule(doctorId);
        return ResponseEntity.ok(schedule);
    }
    @DeleteMapping("/schedule/{id}")
    public ResponseEntity<Void> removeAvailability(@PathVariable Long id) {
        doctorScheduleService.removeDoctorSchedule(id);
        return ResponseEntity.noContent().build();
    }
}