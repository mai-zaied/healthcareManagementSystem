package com.example.healthcareappointmentsystem.service;

import com.example.healthcareappointmentsystem.dto.CreatePatientRequest;
import com.example.healthcareappointmentsystem.entity.Patient;
import com.example.healthcareappointmentsystem.exception.DuplicateResourceException;
import com.example.healthcareappointmentsystem.exception.ResourceNotFoundException;
import com.example.healthcareappointmentsystem.repository.MedicalRecordRepository;
import com.example.healthcareappointmentsystem.repository.PatientRepository;
import com.example.healthcareappointmentsystem.repository.UserRepository;
import com.example.healthcareappointmentsystem.security.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final MedicalRecordService medicalRecordService;

    public Patient getPatientById(Long patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(()->new ResourceNotFoundException("Patient", patientId));
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient updatePatient(Long id, Patient patientUpdates) {
        Patient patient = getPatientById(id);

        if (patientUpdates.getPhoneNumber() != null) {
            patient.setPhoneNumber(patientUpdates.getPhoneNumber());
        }
        if (patientUpdates.getDateOfBirth() != null) {
            patient.setDateOfBirth(patientUpdates.getDateOfBirth());
        }

        return patientRepository.save(patient);
    }

    public Patient createPatient(CreatePatientRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists: " + request.getEmail());
        }

        Patient patient = new Patient();
        patient.setEmail(request.getEmail());
        patient.setPassword(request.getPassword());
        patient.setFullName(request.getFullName());
        patient.setRole(Role.PATIENT);
        patient.setPhoneNumber(request.getPhoneNumber());
        patient.setDateOfBirth(request.getDateOfBirth());
        Patient savedPatient = patientRepository.save(patient);
        medicalRecordService.createMedicalRecordForPatient(savedPatient.getId(), request);
        return savedPatient;
    }
}