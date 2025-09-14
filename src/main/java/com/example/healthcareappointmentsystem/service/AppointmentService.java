package com.example.healthcareappointmentsystem.service;
import com.example.healthcareappointmentsystem.aop.LogOperation;
import com.example.healthcareappointmentsystem.dto.AppointmentResponse;
import com.example.healthcareappointmentsystem.dto.BookAppointmentRequest;
import com.example.healthcareappointmentsystem.entity.*;
import com.example.healthcareappointmentsystem.exception.ResourceNotFoundException;
import com.example.healthcareappointmentsystem.exception.TimeSlotNotAvailableException;
import com.example.healthcareappointmentsystem.repository.AppointmentRepository;
import com.example.healthcareappointmentsystem.repository.DoctorRepository;
import com.example.healthcareappointmentsystem.repository.DoctorScheduleRepository;
import com.example.healthcareappointmentsystem.repository.PatientRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
/**
 * Service layer for managing appointments between patients and doctors.
 */
@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final DoctorScheduleRepository doctorScheduleRepository;
    /**
     *Book a new appointment for a patient with a doctor
     *@param request   the appointment booking request containing doctorId and startTime
     *@param patientId the ID of the patient booking the appointment
     *@return an AppointmentResponse DTO representing the saved appointment
     */
    @LogOperation("BOOK_APPOINTMENT")
    @Transactional
    public AppointmentResponse bookAppointment(BookAppointmentRequest request, Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", patientId));
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", request.getDoctorId()));
        LocalDateTime startTime = request.getStartTime();
        LocalDate date = startTime.toLocalDate();
        DoctorSchedule schedule = doctorScheduleRepository.findByDoctorIdAndDate(doctor.getId(), date)
                .orElseThrow(() -> new TimeSlotNotAvailableException("Doctor has no schedule on this date"));
        int slotMinutes = schedule.getSlotDuration() != null ? schedule.getSlotDuration() : 30;
        LocalTime requestedStart = startTime.toLocalTime();
        boolean validSlot = false;
        LocalTime nextAvailable = null;
        //find if the slot is available if not find the closest slot if exists
        for (LocalTime[] slot : schedule.getAvailableSlots()) {
            LocalTime slotStart = slot[0];
            if (requestedStart.equals(slotStart)) {
                validSlot = true;
                break;
            }
            if (slotStart.isAfter(requestedStart) && nextAvailable == null) {
                nextAvailable = slotStart;
                break;
            }
        }
        if (!validSlot) {
            String msg = (nextAvailable != null)  ?
                    date.atTime(nextAvailable).toString() : "No further slots available today";
            throw new TimeSlotNotAvailableException("Cannot book this time. Next available slot: " + msg);
        }
        LocalDateTime endTime = startTime.plusMinutes(slotMinutes);
        // check overlapping appointments for the doctor
        boolean hasDoctorOverlap = appointmentRepository.existsOverlappingAppointment(request.getDoctorId(), startTime, endTime);
        if (hasDoctorOverlap) {
            throw new TimeSlotNotAvailableException("Time slot is already booked for this doctor");
        }
        // check overlapping appointments for the patient
        boolean hasPatientOverlap = appointmentRepository.existsOverlappingPatientAppointment(patientId, startTime, endTime);
        if (hasPatientOverlap) {
            throw new TimeSlotNotAvailableException("You already have an appointment at this time");
        }
        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setStartTime(startTime);
        appointment.setEndTime(endTime);
        appointment.setStatus(AppointmentStatus.SCHEDULED);

        Appointment saved = appointmentRepository.save(appointment);
        return mapToResponse(saved);
    }
    public List<AppointmentResponse> getPatientAppointments(Long patientId) {
        return appointmentRepository.findByPatientId(patientId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    // Return appointments for doctor as DTOs
    public List<AppointmentResponse> getDoctorAppointments(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    @LogOperation("COMPLETE_APPOINTMENT")
    @Transactional
    public AppointmentResponse markAppointmentCompleted(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", appointmentId));
        appointment.setStatus(AppointmentStatus.COMPLETED);
        Appointment saved = appointmentRepository.save(appointment);
        return mapToResponse(saved);
    }
    @LogOperation("CANCEL_APPOINTMENT")
    @Transactional
    public AppointmentResponse cancelAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", appointmentId));
        appointment.setStatus(AppointmentStatus.CANCELLED);
        Appointment saved = appointmentRepository.save(appointment);
        return mapToResponse(saved);
    }
    /**
     * Converts an Appointment entity into an AppointmentResponse DTO.
     * @param a the Appointment entity to convert
     * @return an AppointmentResponse DTO
     */
    private AppointmentResponse mapToResponse(Appointment a) {
        return new AppointmentResponse(
                a.getId(),
                a.getStartTime(),
                a.getEndTime(),
                a.getStatus() != null ? a.getStatus().name() : null,
                a.getDoctor() != null ? a.getDoctor().getId() : null,
                a.getDoctor() != null ? a.getDoctor().getFullName() : null,
                a.getDoctor() != null ? a.getDoctor().getSpecialty() : null,
                a.getPatient() != null? a.getPatient().getFullName() :null
        );
    }
}