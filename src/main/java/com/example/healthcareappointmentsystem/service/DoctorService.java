package com.example.healthcareappointmentsystem.service;

import com.example.healthcareappointmentsystem.entity.Doctor;
import com.example.healthcareappointmentsystem.exception.ResourceNotFoundException;
import com.example.healthcareappointmentsystem.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final DoctorRepository doctorRepository;
    public List<Doctor> getDoctorsBySpecialty(String specialty){
        List<Doctor> doctors = doctorRepository.findBySpecialty(specialty);
        if (doctors.isEmpty()) {
            throw new ResourceNotFoundException("No doctors found for specialty: " , specialty);
        }
        return doctors;
    }
    public List<String> getAllSpecialties() {
        return doctorRepository.findDistinctSpecialty();
    }
    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", id));
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }
}
