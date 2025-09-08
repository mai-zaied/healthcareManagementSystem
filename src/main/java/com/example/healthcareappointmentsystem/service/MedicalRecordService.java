package com.example.healthcareappointmentsystem.service;

import com.example.healthcareappointmentsystem.collection.MedicalRecord;
import com.example.healthcareappointmentsystem.dto.CreatePatientRequest;
import com.example.healthcareappointmentsystem.exception.ResourceNotFoundException;
import com.example.healthcareappointmentsystem.repository.MedicalRecordRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicalRecordService {
    private final MedicalRecordRepository medicalRecordRepository;
    public MedicalRecord getMedicalRecord(Long id){
        return medicalRecordRepository.findByPatientId(id)
                .orElseThrow(()-> new ResourceNotFoundException("Medical record ", id));
    }
    public MedicalRecord createMedicalRecordForPatient(Long patientId, CreatePatientRequest request) {
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setPatientId(patientId);
        medicalRecord.setAllergies(request.getAllergies());
        medicalRecord.setChronicConditions(request.getChronicConditions());
        medicalRecord.setPrescriptionIds(List.of());
        medicalRecord.setLabResults(List.of());
        return medicalRecordRepository.save(medicalRecord);
    }


    public MedicalRecord addAllergies(Long patientId, List<String> allergies) {
        MedicalRecord medicalRecord = getMedicalRecord(patientId);
        if (medicalRecord.getAllergies() == null) {
            medicalRecord.setAllergies(new ArrayList<>());
        }
        medicalRecord.getAllergies().addAll(allergies);
        return medicalRecordRepository.save(medicalRecord);
    }

    public MedicalRecord addChronicConditions(Long patientId, List<String> conditions) {
        MedicalRecord medicalRecord = getMedicalRecord(patientId);
        if (medicalRecord.getChronicConditions() == null) {
            medicalRecord.setChronicConditions(new ArrayList<>());
        }
        medicalRecord.getChronicConditions().addAll(conditions);
        return medicalRecordRepository.save(medicalRecord);
    }

    public MedicalRecord addLabResults(Long patientId, List<String> labResults) {
        MedicalRecord medicalRecord = getMedicalRecord(patientId);
        if (medicalRecord.getLabResults() == null) {
            medicalRecord.setLabResults(new ArrayList<>());
        }
        medicalRecord.getLabResults().addAll(labResults);
        return medicalRecordRepository.save(medicalRecord);
    }

    @Transactional
    public void addPrescriptionToMedicalRecord(Long patientId, String prescriptionId){
        MedicalRecord medicalRecord = medicalRecordRepository.findByPatientId(patientId)
                .orElseGet(() ->{
                    MedicalRecord newRecord = new MedicalRecord();
                    newRecord.setPatientId(patientId);
                    return medicalRecordRepository.save(newRecord);
                });
        if (medicalRecord.getPrescriptionIds() == null) {
            medicalRecord.setPrescriptionIds(new ArrayList<>());
        }
        medicalRecord.getPrescriptionIds().add(prescriptionId);

        medicalRecordRepository.save(medicalRecord);
    }
}