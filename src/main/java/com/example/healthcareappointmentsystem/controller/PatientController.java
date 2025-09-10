package com.example.healthcareappointmentsystem.controller;

import com.example.healthcareappointmentsystem.collection.MedicalRecord;
import com.example.healthcareappointmentsystem.collection.Prescription;
import com.example.healthcareappointmentsystem.dto.AppointmentResponse;
import com.example.healthcareappointmentsystem.dto.BookAppointmentRequest;
import com.example.healthcareappointmentsystem.dto.DoctorResponse;
import com.example.healthcareappointmentsystem.entity.Appointment;
import com.example.healthcareappointmentsystem.entity.Doctor;
import com.example.healthcareappointmentsystem.entity.Patient;
import com.example.healthcareappointmentsystem.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patient")
@RequiredArgsConstructor
public class PatientController {
    private final AppointmentService appointmentService;
    private final DoctorService doctorService;
    private final PrescriptionService prescriptionService;
    private final MedicalRecordService medicalRecordService;
    private final PatientService patientService;

    //Doctor Search
    @GetMapping("/doctors/specialty/{specialty}")
    public ResponseEntity<List<DoctorResponse>> getDoctorsBySpecialty(@PathVariable String specialty){
        List<DoctorResponse> doctors = doctorService.getDoctorsBySpecialty(specialty);
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/doctors/specialties")
    public ResponseEntity<List<String>> getAllSpecialties(){
        List<String> specialties = doctorService.getAllSpecialties();
        return ResponseEntity.ok(specialties);
    }

    //Appointment Management
    @PostMapping("/appointments")
    public ResponseEntity<AppointmentResponse> bookAppointment(@Valid @RequestBody BookAppointmentRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Long patientId = patientService.findByEmail(email).getId();

        AppointmentResponse appointment = appointmentService.bookAppointment(request, patientId);
        return ResponseEntity.ok(appointment);
    }

    @GetMapping("/appointments")
    public ResponseEntity<List<AppointmentResponse>> getPatientAppointments(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Long patientId = patientService.findByEmail(email).getId();

        List<AppointmentResponse> appointments = appointmentService.getPatientAppointments(patientId);
        return ResponseEntity.ok(appointments);
    }

    @PatchMapping("/appointments/{id}/cancel")
    public ResponseEntity<AppointmentResponse> cancelAppointment(@PathVariable Long id) {
        AppointmentResponse appointment = appointmentService.cancelAppointment(id);
        return ResponseEntity.ok(appointment);
    }

    //Medical Records
    @GetMapping("/prescriptions")
    public ResponseEntity<List<Prescription>> getPatientPrescriptions(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Long patientId = patientService.findByEmail(email).getId();
        List<Prescription> prescriptions = prescriptionService.getPatientPrescriptions(patientId);
        return ResponseEntity.ok(prescriptions);
    }

    @GetMapping("/medical-record")
    public ResponseEntity<MedicalRecord> getMedicalRecord() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Long patientId = patientService.findByEmail(email).getId();
        MedicalRecord medicalRecord = medicalRecordService.getMedicalRecord(patientId);
        return ResponseEntity.ok(medicalRecord);
    }

    // Profile Management
    @GetMapping("/profile")
    public ResponseEntity<Patient> getPatientProfile(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Patient patient = patientService.findByEmail(email);
        return ResponseEntity.ok(patient);
    }

    @PutMapping("/profile")
    public ResponseEntity<Patient> updatePatientProfile(@RequestBody Patient patientUpdates){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Long patientId = patientService.findByEmail(email).getId();
        Patient patient = patientService.updatePatient(patientId, patientUpdates);
        return ResponseEntity.ok(patient);
    }
}