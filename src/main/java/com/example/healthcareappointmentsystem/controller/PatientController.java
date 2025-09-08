
package com.example.healthcareappointmentsystem.controller;

import com.example.healthcareappointmentsystem.collection.MedicalRecord;
import com.example.healthcareappointmentsystem.collection.Prescription;
import com.example.healthcareappointmentsystem.dto.BookAppointmentRequest;
import com.example.healthcareappointmentsystem.entity.Appointment;
import com.example.healthcareappointmentsystem.entity.Doctor;
import com.example.healthcareappointmentsystem.entity.Patient;
import com.example.healthcareappointmentsystem.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patient")
@RequiredArgsConstructor
public class PatientController {
    private final AppointmentService appointmentService;
    private final DoctorService doctorService;
    private final PrescriptionService prescriptionService;
    private final MedicalRecordService medicalRecordService;
    private final PatientService patientService;

    //Doctor Search
    @GetMapping("/doctors/specialty/{specialty}")
    public ResponseEntity<List<Doctor>> getDoctorsBySpecialty(@PathVariable String specialty){
        List<Doctor> doctors = doctorService.getDoctorsBySpecialty(specialty);
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/doctors/specialties")
    public ResponseEntity<List<String>> getAllSpecialties(){
        List<String> specialties = doctorService.getAllSpecialties();
        return ResponseEntity.ok(specialties);
    }

    //Appointment Management
    @PostMapping("/appointments")
    public ResponseEntity<Appointment> bookAppointment(@Valid @RequestBody BookAppointmentRequest request){
        Appointment appointment =  appointmentService.bookAppointment(request);
        return ResponseEntity.ok(appointment);
    }

    @GetMapping("/appointments")
    public ResponseEntity<List<Appointment>> getPatientAppointments(@RequestParam Long patientId){
        List<Appointment> appointments = appointmentService.getPatientAppointments(patientId);
        return ResponseEntity.ok(appointments);
    }

    @PatchMapping("/appointments/{id}/cancel")
    public ResponseEntity<Appointment> cancelAppointment(@PathVariable Long id){
        Appointment appointment = appointmentService.cancelAppointment(id);
        return ResponseEntity.ok(appointment);
    }

    //Medical Records
    @GetMapping("/prescriptions")
    public ResponseEntity<List<Prescription>> getPatientPrescriptions(@RequestParam Long patientId){
        List<Prescription> prescriptions = prescriptionService.getPatientPrescriptions(patientId);
        return ResponseEntity.ok(prescriptions);
    }

    @GetMapping("/medical-record")
    public ResponseEntity<MedicalRecord> getMedicalRecord(@RequestParam Long patientId) {
        MedicalRecord medicalRecord = medicalRecordService.getMedicalRecord(patientId);
        return ResponseEntity.ok(medicalRecord);
    }

    // Profile Management
    @GetMapping("/profile")
    public ResponseEntity<Patient> getPatientProfile(@RequestParam Long patientId){
        Patient patient = patientService.getPatientById(patientId);
        return ResponseEntity.ok(patient);
    }

    @PutMapping("/profile")
    public ResponseEntity<Patient> updatePatientProfile(@RequestParam Long patientId,
                                                        @RequestBody Patient patientUpdates){
        Patient patient = patientService.updatePatient(patientId, patientUpdates);
        return ResponseEntity.ok(patient);
    }
}