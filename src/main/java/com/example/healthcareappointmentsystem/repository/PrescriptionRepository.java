package com.example.healthcareappointmentsystem.repository;

import com.example.healthcareappointmentsystem.document.Prescription;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface PrescriptionRepository extends MongoRepository<Prescription, String> {
    List<Prescription> findByPatientId(Long patientId);
    List<Prescription> findByDoctorId(Long doctorId);
}