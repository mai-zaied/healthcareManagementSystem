package com.example.healthcareappointmentsystem.service;

import com.example.healthcareappointmentsystem.dto.BookAppointmentRequest;
import com.example.healthcareappointmentsystem.entity.*;
import com.example.healthcareappointmentsystem.exception.ResourceNotFoundException;
import com.example.healthcareappointmentsystem.exception.TimeSlotNotAvailableException;
import com.example.healthcareappointmentsystem.repository.*;
import com.example.healthcareappointmentsystem.service.AppointmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private DoctorRepository doctorRepository;
    @Mock
    private PatientRepository patientRepository;
    @Mock
    private DoctorScheduleRepository doctorScheduleRepository;

    @InjectMocks
    private AppointmentService appointmentService;

    private Patient patient;
    private Doctor doctor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        patient = new Patient();
        patient.setId(1L);
        patient.setFullName("Mai Test");

        doctor = new Doctor();
        doctor.setId(2L);
        doctor.setFullName("Dr. John");
        doctor.setSpecialty("Cardiology");
    }

    @Test
    void bookAppointment_successful() {
        Long patientId = 1L;
        LocalDateTime startTime = LocalDateTime.of(2024, 1, 15, 10, 0);

        BookAppointmentRequest request = new BookAppointmentRequest();
        request.setDoctorId(2L);
        request.setStartTime(startTime);

        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(2L)).thenReturn(Optional.of(doctor));
        when(doctorScheduleRepository.isTimeSlotAvailable(eq(2L), any(LocalDate.class), any(LocalTime.class), any(LocalTime.class)))
                .thenReturn(Optional.of(mock(DoctorSchedule.class)));
        when(appointmentRepository.existsOverlappingAppointment(eq(2L), any(), any())).thenReturn(false);
        when(appointmentRepository.existsOverlappingPatientAppointment(eq(1L), any(), any())).thenReturn(false);

        Appointment appointment = new Appointment();
        appointment.setId(99L);
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setStartTime(startTime);
        appointment.setEndTime(startTime.plusMinutes(30));
        appointment.setStatus(AppointmentStatus.SCHEDULED);

        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);

        var response = appointmentService.bookAppointment(request, patientId);

        assertNotNull(response);
        assertEquals(99L, response.getAppointmentId());
        assertEquals("SCHEDULED", response.getStatus());
        assertEquals("Mai Test", response.getPatientFullName());
        verify(appointmentRepository, times(1)).save(any(Appointment.class));
    }

    @Test
    void bookAppointment_patientNotFound() {
        BookAppointmentRequest request = new BookAppointmentRequest();
        request.setDoctorId(2L);
        request.setStartTime(LocalDateTime.now().plusDays(1));

        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> appointmentService.bookAppointment(request, 1L));
    }

    @Test
    void bookAppointment_doctorNotFound() {
        BookAppointmentRequest request = new BookAppointmentRequest();
        request.setDoctorId(2L);
        request.setStartTime(LocalDateTime.now().plusDays(1));

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> appointmentService.bookAppointment(request, 1L));
    }

    @Test
    void bookAppointment_doctorNotAvailable() {
        BookAppointmentRequest request = new BookAppointmentRequest();
        request.setDoctorId(2L);
        request.setStartTime(LocalDateTime.now().plusDays(1));

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(2L)).thenReturn(Optional.of(doctor));
        when(doctorScheduleRepository.isTimeSlotAvailable(eq(2L), any(), any(), any()))
                .thenReturn(Optional.empty());

        assertThrows(TimeSlotNotAvailableException.class,
                () -> appointmentService.bookAppointment(request, 1L));
    }
    @Test
    void bookAppointment_doctorOverlap() {
        BookAppointmentRequest request = new BookAppointmentRequest();
        request.setDoctorId(2L);
        request.setStartTime(LocalDateTime.now().plusDays(1));

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(2L)).thenReturn(Optional.of(doctor));
        when(doctorScheduleRepository.isTimeSlotAvailable(any(), any(), any(), any()))
                .thenReturn(Optional.of(mock(DoctorSchedule.class)));
        when(appointmentRepository.existsOverlappingAppointment(any(), any(), any()))
                .thenReturn(true);

        assertThrows(TimeSlotNotAvailableException.class,
                () -> appointmentService.bookAppointment(request, 1L));
    }
    @Test
    void bookAppointment_patientOverlap() {
        BookAppointmentRequest request = new BookAppointmentRequest();
        request.setDoctorId(2L);
        request.setStartTime(LocalDateTime.now().plusDays(1));

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(2L)).thenReturn(Optional.of(doctor));
        when(doctorScheduleRepository.isTimeSlotAvailable(any(), any(), any(), any()))
                .thenReturn(Optional.of(mock(DoctorSchedule.class)));
        when(appointmentRepository.existsOverlappingAppointment(any(), any(), any()))
                .thenReturn(false);
        when(appointmentRepository.existsOverlappingPatientAppointment(any(), any(), any()))
                .thenReturn(true);

        assertThrows(TimeSlotNotAvailableException.class,
                () -> appointmentService.bookAppointment(request, 1L));
    }

    @Test
    void markAppointmentCompleted_success() {
        Appointment appointment = new Appointment();
        appointment.setId(5L);
        appointment.setStatus(AppointmentStatus.SCHEDULED);

        when(appointmentRepository.findById(5L)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(any())).thenReturn(appointment);

        var response = appointmentService.markAppointmentCompleted(5L);

        assertEquals("COMPLETED", response.getStatus());
        verify(appointmentRepository).save(appointment);
    }

    @Test
    void markAppointmentCompleted_notFound() {
        when(appointmentRepository.findById(5L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> appointmentService.markAppointmentCompleted(5L));
    }

    @Test
    void cancelAppointment_success() {
        Appointment appointment = new Appointment();
        appointment.setId(6L);
        appointment.setStatus(AppointmentStatus.SCHEDULED);

        when(appointmentRepository.findById(6L)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(any())).thenReturn(appointment);

        var response = appointmentService.cancelAppointment(6L);

        assertEquals("CANCELLED", response.getStatus());
        verify(appointmentRepository).save(appointment);
    }

    @Test
    void cancelAppointment_notFound() {
        when(appointmentRepository.findById(6L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> appointmentService.cancelAppointment(6L));
    }
}
