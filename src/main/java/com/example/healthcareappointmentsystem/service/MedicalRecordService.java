package com.example.healthcareappointmentsystem.service;

import com.example.healthcareappointmentsystem.collection.MedicalRecord;
import com.example.healthcareappointmentsystem.exception.ResourceNotFoundException;
import com.example.healthcareappointmentsystem.repository.MedicalRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MedicalRecordService {
    private final MedicalRecordRepository medicalRecordRepository;
    public MedicalRecord getMedicalRecord(Long id){
        return medicalRecordRepository.findByPatientId(id)
                .orElseThrow(()-> new ResourceNotFoundException("Medical record ", id));
    }
}

