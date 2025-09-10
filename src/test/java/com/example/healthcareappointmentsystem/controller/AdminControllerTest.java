package com.example.healthcareappointmentsystem.controller;

import com.example.healthcareappointmentsystem.dto.*;
import com.example.healthcareappointmentsystem.entity.Doctor;
import com.example.healthcareappointmentsystem.entity.Patient;
import com.example.healthcareappointmentsystem.exception.DuplicateResourceException;
import com.example.healthcareappointmentsystem.exception.ResourceNotFoundException;
import com.example.healthcareappointmentsystem.service.DoctorService;
import com.example.healthcareappointmentsystem.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminControllerTest {

    @Mock
    private DoctorService doctorService;

    @Mock
    private PatientService patientService;

    @InjectMocks
    private AdminController adminController;

    private Doctor doctor;
    private DoctorResponse doctorResponse;

    private Patient patient;
    private PatientResponse patientResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        doctor = new Doctor();
        doctor.setId(1L);
        doctor.setEmail("dr.smith@hospital.com");
        doctor.setFullName("Dr. John Smith");
        doctor.setPhoneNumber("555-1234");
        doctor.setSpecialty("Cardiology");

        doctorResponse = new DoctorResponse(
                doctor.getId(),
                doctor.getEmail(),
                doctor.getFullName(),
                "DOCTOR",
                doctor.getSpecialty(),
                doctor.getPhoneNumber()
        );

        patient = new Patient();
        patient.setId(1L);
        patient.setEmail("patient.jane@hospital.com");
        patient.setFullName("Jane Smith");
        patient.setPhoneNumber("555-1002");
        patient.setDateOfBirth(LocalDate.of(1985, 5, 15));

        patientResponse = new PatientResponse(
                patient.getId(),
                patient.getEmail(),
                patient.getFullName(),
                "PATIENT",
                patient.getPhoneNumber(),
                patient.getDateOfBirth()
        );
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetAllDoctors() {
        when(doctorService.getAllDoctors()).thenReturn(List.of(doctorResponse));

        ResponseEntity<List<DoctorResponse>> response = adminController.getAllDoctors();

        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(doctorService, times(1)).getAllDoctors();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetDoctorById() {
        when(doctorService.getDoctorById(1L)).thenReturn(doctorResponse);

        ResponseEntity<DoctorResponse> response = adminController.getDoctorById(1L);

        assertEquals(doctorResponse.getEmail(), response.getBody().getEmail());
        verify(doctorService, times(1)).getDoctorById(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetDoctorById_NotFound() {
        when(doctorService.getDoctorById(99L))
                .thenThrow(new ResourceNotFoundException("Doctor", 99L));

        assertThrows(ResourceNotFoundException.class, () -> adminController.getDoctorById(99L));
        verify(doctorService, times(1)).getDoctorById(99L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateDoctor() {
        when(doctorService.createDoctor(any())).thenReturn(doctor);

        CreateDoctorRequest request = new CreateDoctorRequest();
        request.setEmail("dr.smith@hospital.com");
        request.setFullName("Dr. John Smith");
        request.setSpecialty("Cardiology");
        request.setPhoneNumber("555-1234");
        request.setPassword("password");

        ResponseEntity<Doctor> response = adminController.createDoctor(request);

        assertEquals("dr.smith@hospital.com", response.getBody().getEmail());
        verify(doctorService, times(1)).createDoctor(request);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateDoctor_DuplicateEmail() {
        when(doctorService.createDoctor(any()))
                .thenThrow(new DuplicateResourceException("Email already exists: dr.smith@hospital.com"));

        CreateDoctorRequest request = new CreateDoctorRequest();
        request.setEmail("dr.smith@hospital.com");

        assertThrows(DuplicateResourceException.class, () -> adminController.createDoctor(request));
        verify(doctorService, times(1)).createDoctor(request);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateDoctor() {
        UpdateDoctorRequest request = new UpdateDoctorRequest();
        request.setFullName("Dr. John Updated");

        doctor.setFullName(request.getFullName());
        when(doctorService.updateDoctor(1L, request)).thenReturn(doctor);

        ResponseEntity<Doctor> response = adminController.updateDoctor(1L, request);

        assertEquals("Dr. John Updated", response.getBody().getFullName());
        verify(doctorService, times(1)).updateDoctor(1L, request);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteDoctor() {
        doNothing().when(doctorService).deleteDoctor(1L);

        ResponseEntity<Void> response = adminController.deleteDoctor(1L);

        assertEquals(204, response.getStatusCodeValue());
        verify(doctorService, times(1)).deleteDoctor(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetAllPatients() {
        when(patientService.getAllPatients()).thenReturn(List.of(patientResponse));

        ResponseEntity<List<PatientResponse>> response = adminController.getAllPatients();

        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(patientService, times(1)).getAllPatients();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetPatientById() {
        when(patientService.getPatientById(1L)).thenReturn(patientResponse);

        ResponseEntity<PatientResponse> response = adminController.getPatientById(1L);

        assertEquals(patientResponse.getEmail(), response.getBody().getEmail());
        verify(patientService, times(1)).getPatientById(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetPatientById_NotFound() {
        when(patientService.getPatientById(99L))
                .thenThrow(new ResourceNotFoundException("Patient", 99L));

        assertThrows(ResourceNotFoundException.class, () -> adminController.getPatientById(99L));
        verify(patientService, times(1)).getPatientById(99L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreatePatient() {
        CreatePatientRequest request = new CreatePatientRequest();
        request.setEmail("patient.jane@hospital.com");
        request.setFullName("Jane Smith");
        request.setPhoneNumber("555-1002");
        request.setDateOfBirth(LocalDate.of(1985, 5, 15));
        request.setPassword("password");

        when(patientService.createPatient(request)).thenReturn(patient);

        ResponseEntity<Patient> response = adminController.createPatient(request);

        assertEquals("patient.jane@hospital.com", response.getBody().getEmail());
        verify(patientService, times(1)).createPatient(request);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreatePatient_DuplicateEmail() {
        CreatePatientRequest request = new CreatePatientRequest();
        request.setEmail("patient.jane@hospital.com");

        when(patientService.createPatient(request))
                .thenThrow(new DuplicateResourceException("Email already exists: patient.jane@hospital.com"));

        assertThrows(DuplicateResourceException.class, () -> adminController.createPatient(request));
        verify(patientService, times(1)).createPatient(request);
    }
}
