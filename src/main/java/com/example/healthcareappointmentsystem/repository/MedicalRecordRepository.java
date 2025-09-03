package com.example.healthcareappointmentsystem.repository;

import com.example.healthcareappointmentsystem.document.MedicalRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface MedicalRecordRepository extends MongoRepository<MedicalRecord, String> {
    Optional<MedicalRecord> findByPatientId(Long patientId);
}