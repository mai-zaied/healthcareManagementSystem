package com.example.healthcareappointmentsystem.service;

import com.example.healthcareappointmentsystem.aop.LogOperation;
import com.example.healthcareappointmentsystem.collection.Prescription;
import com.example.healthcareappointmentsystem.dto.CreatePrescriptionRequest;
import com.example.healthcareappointmentsystem.exception.ResourceNotFoundException;
import com.example.healthcareappointmentsystem.repository.PrescriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PrescriptionService {
    private final PrescriptionRepository prescriptionRepository;
    private final MedicalRecordService medicalRecordService;

    @LogOperation("CREATE_PRESCRIPTION")
    public Prescription createPrescription(CreatePrescriptionRequest request){
        Prescription prescription = new Prescription();
        prescription.setPatientId(request.getPatientId());
        prescription.setDoctorId(request.getDoctorId());
        prescription.setAppointmentId(request.getAppointmentId());
        prescription.setDiagnosis(request.getDiagnosis());
        prescription.setNotes(request.getNotes());
        prescription.setMedicines(request.getMedicines());

        Prescription savedPrescription = prescriptionRepository.save(prescription);
        medicalRecordService.addPrescriptionToMedicalRecord(
                request.getPatientId(),
                savedPrescription.getId()
        );

        return savedPrescription;
    }
    public List<Prescription> getPatientPrescriptions(Long patientId) {
        List<Prescription> prescriptions = prescriptionRepository.findByPatientId(patientId);
        if (prescriptions.isEmpty()) {
            throw new ResourceNotFoundException(" prescriptions ", patientId);
        }
        return prescriptions;
    }

    public List<Prescription> getDoctorPrescriptions(Long doctorId) {
        return prescriptionRepository.findByDoctorId(doctorId);
    }
}