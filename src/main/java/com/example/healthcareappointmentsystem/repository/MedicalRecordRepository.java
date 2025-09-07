package com.example.healthcareappointmentsystem.repository;

import com.example.healthcareappointmentsystem.collection.MedicalRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface MedicalRecordRepository extends MongoRepository<MedicalRecord, String> {
    Optional<MedicalRecord> findByPatientId(Long patientId);
}