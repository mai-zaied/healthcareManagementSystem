package com.example.healthcareappointmentsystem.controller;

import com.example.healthcareappointmentsystem.dto.CreateDoctorRequest;
import com.example.healthcareappointmentsystem.dto.CreatePatientRequest;
import com.example.healthcareappointmentsystem.entity.Doctor;
import com.example.healthcareappointmentsystem.entity.Patient;
import com.example.healthcareappointmentsystem.service.DoctorService;
import com.example.healthcareappointmentsystem.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final DoctorService doctorService;
    private final PatientService patientService;

    // Doctor Management
    @GetMapping("/doctors")
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        List<Doctor> doctors = doctorService.getAllDoctors();
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/doctors/{id}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable Long id) {
        Doctor doctor = doctorService.getDoctorById(id);
        return ResponseEntity.ok(doctor);
    }
    @PostMapping("/doctors")
    public ResponseEntity<Doctor> createDoctor(@Valid @RequestBody CreateDoctorRequest request) {
        Doctor doctor = doctorService.createDoctor(request);
        return ResponseEntity.ok(doctor);
    }
    @PutMapping("/doctors/{id}")
    public ResponseEntity<Doctor> updateDoctor(@PathVariable Long id,
                                               @Valid @RequestBody CreateDoctorRequest request) {
        Doctor doctor = doctorService.updateDoctor(id, request);
        return ResponseEntity.ok(doctor);
    }
    @DeleteMapping("/doctors/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
        return ResponseEntity.noContent().build();
    }
    // Patient Management
    @GetMapping("/patients")
    public ResponseEntity<List<Patient>> getAllPatients() {
        List<Patient> patients = patientService.getAllPatients();
        return ResponseEntity.ok(patients);
    }
    @GetMapping("/patients/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        Patient patient = patientService.getPatientById(id);
        return ResponseEntity.ok(patient);
    }
    @PostMapping("/patients")
    public ResponseEntity<Patient> createPatient(@Valid @RequestBody CreatePatientRequest request) {
        Patient patient = patientService.createPatient(request);
        return ResponseEntity.ok(patient);
    }
}