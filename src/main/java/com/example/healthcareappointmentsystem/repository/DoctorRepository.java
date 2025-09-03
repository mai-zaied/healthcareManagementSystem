package com.example.healthcareappointmentsystem.repository;

import com.example.healthcareappointmentsystem.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}
