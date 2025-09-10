package com.example.healthcareappointmentsystem.controller;

import com.example.healthcareappointmentsystem.collection.MedicalRecord;
import com.example.healthcareappointmentsystem.collection.Prescription;
import com.example.healthcareappointmentsystem.dto.AppointmentResponse;
import com.example.healthcareappointmentsystem.dto.BookAppointmentRequest;
import com.example.healthcareappointmentsystem.dto.DoctorResponse;
import com.example.healthcareappointmentsystem.entity.AppointmentStatus;
import com.example.healthcareappointmentsystem.entity.Patient;
import com.example.healthcareappointmentsystem.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class PatientControllerTest {

    @Mock
    private AppointmentService appointmentService;

    @Mock
    private DoctorService doctorService;

    @Mock
    private PrescriptionService prescriptionService;

    @Mock
    private MedicalRecordService medicalRecordService;

    @Mock
    private PatientService patientService;

    @InjectMocks
    private PatientController patientController;

    private Patient testPatient;
    private final String testEmail = "patient@example.com";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testPatient = new Patient();
        testPatient.setId(1L);
        testPatient.setEmail(testEmail);
        testPatient.setFullName("John Doe");

        Authentication authentication = new UsernamePasswordAuthenticationToken(testEmail, "password");
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(patientService.findByEmail(testEmail)).thenReturn(testPatient);
    }

    @Test
    void getDoctorsBySpecialty_Success() {
        String specialty = "Cardiology";
        List<DoctorResponse> expectedDoctors = Arrays.asList(
                new DoctorResponse(1L, "doc1@example.com", "Dr. Smith", "DOCTOR", "Cardiology", "123-456-7890"),
                new DoctorResponse(2L, "doc2@example.com", "Dr. Johnson", "DOCTOR", "Cardiology", "098-765-4321")
        );

        when(doctorService.getDoctorsBySpecialty(specialty)).thenReturn(expectedDoctors);

        ResponseEntity<List<DoctorResponse>> response = patientController.getDoctorsBySpecialty(specialty);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedDoctors, response.getBody());
        verify(doctorService, times(1)).getDoctorsBySpecialty(specialty);
    }

    @Test
    void getAllSpecialties_Success() {
        List<String> expectedSpecialties = Arrays.asList("Cardiology", "Neurology", "Pediatrics");
        when(doctorService.getAllSpecialties()).thenReturn(expectedSpecialties);

        ResponseEntity<List<String>> response = patientController.getAllSpecialties();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedSpecialties, response.getBody());
        verify(doctorService, times(1)).getAllSpecialties();
    }

    @Test
    void bookAppointment_Success() {
        BookAppointmentRequest request = new BookAppointmentRequest();
        request.setDoctorId(1L);
        request.setStartTime(LocalDateTime.now().plusDays(1));

        AppointmentResponse expectedResponse = new AppointmentResponse(
                1L,
                request.getStartTime(),
                request.getStartTime().plusMinutes(30),
                AppointmentStatus.SCHEDULED.name(),
                1L,
                "Dr. Smith",
                "Cardiology",
                "John Doe"
        );

        when(appointmentService.bookAppointment(eq(request), eq(1L))).thenReturn(expectedResponse);
        ResponseEntity<AppointmentResponse> response = patientController.bookAppointment(request);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedResponse, response.getBody());
        verify(patientService, times(1)).findByEmail(testEmail);
        verify(appointmentService, times(1)).bookAppointment(request, 1L);
    }

    @Test
    void getPatientAppointments_Success() {
        List<AppointmentResponse> expectedAppointments = Arrays.asList(
                new AppointmentResponse(1L, LocalDateTime.now(), LocalDateTime.now().plusMinutes(30),
                        AppointmentStatus.SCHEDULED.name(), 1L, "Dr. Smith", "Cardiology", "John Doe"),
                new AppointmentResponse(2L, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusMinutes(30),
                        AppointmentStatus.SCHEDULED.name(), 2L, "Dr. Johnson", "Neurology", "John Doe")
        );

        when(appointmentService.getPatientAppointments(1L)).thenReturn(expectedAppointments);
        ResponseEntity<List<AppointmentResponse>> response = patientController.getPatientAppointments();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedAppointments, response.getBody());
        verify(patientService, times(1)).findByEmail(testEmail);
        verify(appointmentService, times(1)).getPatientAppointments(1L);
    }

    @Test
    void cancelAppointment_Success() {
        Long appointmentId = 1L;
        AppointmentResponse expectedResponse = new AppointmentResponse(
                appointmentId, LocalDateTime.now(), LocalDateTime.now().plusMinutes(30),
                AppointmentStatus.CANCELLED.name(), 1L, "Dr. Smith", "Cardiology", "John Doe"
        );

        when(appointmentService.cancelAppointment(appointmentId)).thenReturn(expectedResponse);
        ResponseEntity<AppointmentResponse> response = patientController.cancelAppointment(appointmentId);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedResponse, response.getBody());
        verify(appointmentService, times(1)).cancelAppointment(appointmentId);
    }

    @Test
    void getMedicalRecord_Success() {
        MedicalRecord expectedMedicalRecord = new MedicalRecord();
        expectedMedicalRecord.setPatientId(1L);
        expectedMedicalRecord.setAllergies(List.of("Pollen", "Dust"));
        expectedMedicalRecord.setChronicConditions(List.of("Asthma"));

        when(medicalRecordService.getMedicalRecord(1L)).thenReturn(expectedMedicalRecord);

        ResponseEntity<MedicalRecord> response = patientController.getMedicalRecord();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedMedicalRecord, response.getBody());
        verify(patientService, times(1)).findByEmail(testEmail);
        verify(medicalRecordService, times(1)).getMedicalRecord(1L);
    }

    @Test
    void getPatientProfile_Success() {
        when(patientService.findByEmail(testEmail)).thenReturn(testPatient);
        ResponseEntity<Patient> response = patientController.getPatientProfile();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(testPatient, response.getBody());
        verify(patientService, times(1)).findByEmail(testEmail);
    }

    @Test
    void updatePatientProfile_Success() {
        Patient patientUpdates = new Patient();
        patientUpdates.setPhoneNumber("123-456-7890");
        patientUpdates.setDateOfBirth(LocalDate.of(1990, 1, 1));

        Patient updatedPatient = new Patient();
        updatedPatient.setId(1L);
        updatedPatient.setEmail(testEmail);
        updatedPatient.setFullName("John Doe");
        updatedPatient.setPhoneNumber("123-456-7890");
        updatedPatient.setDateOfBirth(LocalDate.of(1990, 1, 1));

        when(patientService.updatePatient(eq(1L), eq(patientUpdates))).thenReturn(updatedPatient);
        ResponseEntity<Patient> response = patientController.updatePatientProfile(patientUpdates);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(updatedPatient, response.getBody());
        verify(patientService, times(1)).findByEmail(testEmail);
        verify(patientService, times(1)).updatePatient(1L, patientUpdates);
    }

    @Test
    void bookAppointment_AuthenticationFailure() {
        SecurityContextHolder.clearContext();

        BookAppointmentRequest request = new BookAppointmentRequest();

        assertThrows(Exception.class, () -> patientController.bookAppointment(request));
    }

    @Test
    void getPatientAppointments_NoAppointments() {
        when(appointmentService.getPatientAppointments(1L)).thenReturn(List.of());

        ResponseEntity<List<AppointmentResponse>> response = patientController.getPatientAppointments();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isEmpty());
        verify(patientService, times(1)).findByEmail(testEmail);
        verify(appointmentService, times(1)).getPatientAppointments(1L);
    }
}