package com.example.healthcareappointmentsystem.service;

import com.example.healthcareappointmentsystem.aop.LogOperation;
import com.example.healthcareappointmentsystem.dto.BookAppointmentRequest;
import com.example.healthcareappointmentsystem.entity.Appointment;
import com.example.healthcareappointmentsystem.entity.AppointmentStatus;
import com.example.healthcareappointmentsystem.entity.Doctor;
import com.example.healthcareappointmentsystem.entity.Patient;
import com.example.healthcareappointmentsystem.exception.ResourceNotFoundException;
import com.example.healthcareappointmentsystem.exception.TimeSlotNotAvailableException;
import com.example.healthcareappointmentsystem.repository.AppointmentRepository;
import com.example.healthcareappointmentsystem.repository.DoctorRepository;
import com.example.healthcareappointmentsystem.repository.DoctorScheduleRepository;
import com.example.healthcareappointmentsystem.repository.PatientRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final DoctorScheduleRepository doctorScheduleRepository;

    @LogOperation("BOOK_APPOINTMENT")
    @Transactional
    public Appointment bookAppointment( BookAppointmentRequest request){
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient", request.getPatientId()));
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", request.getDoctorId()));
        LocalDateTime startTime = request.getStartTime();
        LocalDateTime endTime = startTime.plusMinutes(30);
        LocalDate date = startTime.toLocalDate();

        // make sure the time slot fits doctor's schedule
        boolean fitsInSchedule = doctorScheduleRepository.isTimeSlotAvailable(
                request.getDoctorId(),date,startTime.toLocalTime(),endTime.toLocalTime()
        ).isPresent();
        if(!fitsInSchedule){
            throw new TimeSlotNotAvailableException("Doctor is not available at this time");
        }

        //check overlapping appointments
        boolean hasOverlap = appointmentRepository.existsOverlappingAppointment(
                request.getDoctorId(),startTime,endTime
        );
        if(hasOverlap){
            throw new TimeSlotNotAvailableException("Time slot is already booked");
        }

        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setStartTime(startTime);
        appointment.setEndTime(endTime);
        appointment.setStatus(AppointmentStatus.SCHEDULED);

        return appointmentRepository.save(appointment);
    }

    public List<Appointment> getPatientAppointments(Long patientId){
        return appointmentRepository.findByPatientId(patientId);
    }

    public List<Appointment> getDoctorAppointments(Long doctorId){
        return appointmentRepository.findByDoctorId(doctorId);
    }

    @LogOperation("COMPLETE_APPOINTMENT")
    @Transactional
    public Appointment markAppointmentCompleted(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", appointmentId));

        appointment.setStatus(AppointmentStatus.COMPLETED);
        return appointmentRepository.save(appointment);
    }

    @LogOperation("CANCEL_APPOINTMENT")
    @Transactional
    public Appointment cancelAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", appointmentId));

        appointment.setStatus(AppointmentStatus.CANCELLED);
        return appointmentRepository.save(appointment);
    }

}