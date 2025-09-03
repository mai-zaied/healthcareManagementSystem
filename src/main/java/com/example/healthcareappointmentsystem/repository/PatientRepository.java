package com.example.healthcareappointmentsystem.repository;

import com.example.healthcareappointmentsystem.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}