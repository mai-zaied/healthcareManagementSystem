package com.example.healthcareappointmentsystem.service;

import com.example.healthcareappointmentsystem.dto.CreateDoctorRequest;
import com.example.healthcareappointmentsystem.dto.DoctorResponse;
import com.example.healthcareappointmentsystem.dto.UpdateDoctorRequest;
import com.example.healthcareappointmentsystem.entity.Doctor;
import com.example.healthcareappointmentsystem.entity.User;
import com.example.healthcareappointmentsystem.exception.DuplicateResourceException;
import com.example.healthcareappointmentsystem.exception.ResourceNotFoundException;
import com.example.healthcareappointmentsystem.repository.DoctorRepository;
import com.example.healthcareappointmentsystem.repository.UserRepository;
import com.example.healthcareappointmentsystem.security.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<DoctorResponse> getDoctorsBySpecialty(String specialty) {
        List<Doctor> doctors = doctorRepository.findBySpecialty(specialty);

        if (doctors.isEmpty()) {
            throw new ResourceNotFoundException("No doctors found for specialty: ", specialty);
        }

        return doctors.stream()
                .map(doctor -> new DoctorResponse(
                        doctor.getId(),
                        doctor.getEmail(),
                        doctor.getFullName(),
                        doctor.getRole().toString(),
                        doctor.getSpecialty(),
                        doctor.getPhoneNumber()
                ))
                .toList();
    }

    public List<String> getAllSpecialties() {
        return doctorRepository.findDistinctSpecialty();
    }

    public DoctorResponse getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", id));

        return new DoctorResponse(
                doctor.getId(),
                doctor.getEmail(),
                doctor.getFullName(),
                doctor.getRole().name(),
                doctor.getSpecialty(),
                doctor.getPhoneNumber()
        );
    }

    public List<DoctorResponse> getAllDoctors() {
        return doctorRepository.findAllByDeletedFalse().stream()
                .map(doctor -> new DoctorResponse(
                        doctor.getId(),
                        doctor.getEmail(),
                        doctor.getFullName(),
                        doctor.getRole().name(),
                        doctor.getSpecialty(),
                        doctor.getPhoneNumber()
                ))
                .toList();
    }

    public Doctor createDoctor(CreateDoctorRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists: " + request.getEmail());
        }
        Doctor doctor = new Doctor();
        doctor.setEmail(request.getEmail());
        doctor.setPassword(passwordEncoder.encode(request.getPassword()));
        doctor.setFullName(request.getFullName());
        doctor.setRole(Role.DOCTOR);
        doctor.setSpecialty(request.getSpecialty());
        doctor.setPhoneNumber(request.getPhoneNumber());
        return doctorRepository.save(doctor);
    }

    public void deleteDoctor(Long id) {
        Doctor doctor = doctorRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", id));

        doctor.setDeleted(true); // soft delete
        doctorRepository.save(doctor);
    }


    public Doctor updateDoctor(Long id, UpdateDoctorRequest request) {
        Doctor doctor =doctorRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", id));

        if (request.getEmail() != null && !request.getEmail().equals(doctor.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new DuplicateResourceException("Email already exists: " + request.getEmail());
            }
            doctor.setEmail(request.getEmail());
        }

        if (request.getPassword() != null) {
            doctor.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        if (request.getFullName() != null) {
            doctor.setFullName(request.getFullName());
        }
        if (request.getSpecialty() != null) {
            doctor.setSpecialty(request.getSpecialty());
        }
        if (request.getPhoneNumber() != null) {
            doctor.setPhoneNumber(request.getPhoneNumber());
        }

        return doctorRepository.save(doctor);
    }

    public Doctor findByEmail(String email) {
        return doctorRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with email: " , email));
    }

}