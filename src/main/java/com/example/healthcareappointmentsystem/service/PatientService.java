package com.example.healthcareappointmentsystem.service;

import com.example.healthcareappointmentsystem.entity.Patient;
import com.example.healthcareappointmentsystem.exception.ResourceNotFoundException;
import com.example.healthcareappointmentsystem.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
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
}
