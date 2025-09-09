package com.example.healthcareappointmentsystem.service;

import com.example.healthcareappointmentsystem.dto.CreateDoctorRequest;
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

    public Doctor createDoctor(CreateDoctorRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists: " + request.getEmail());
        }
        Doctor doctor = new Doctor();
        doctor.setEmail(request.getEmail());
        doctor.setPassword(passwordEncoder.encode(request.getPassword())); // Encrypt password
        doctor.setFullName(request.getFullName());
        doctor.setRole(Role.DOCTOR);
        doctor.setSpecialty(request.getSpecialty());
        doctor.setPhoneNumber(request.getPhoneNumber());
        return doctorRepository.save(doctor);
    }
    public void deleteDoctor(Long id) {
        if (!doctorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Doctor", id);
        }
        doctorRepository.deleteById(id);
    }

    public Doctor updateDoctor(Long id, CreateDoctorRequest request) {
        Doctor doctor = getDoctorById(id);

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