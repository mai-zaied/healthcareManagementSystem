package com.example.healthcareappointmentsystem.service;
import com.example.healthcareappointmentsystem.aop.LogOperation;
import com.example.healthcareappointmentsystem.collection.Prescription;
import com.example.healthcareappointmentsystem.dto.CreatePrescriptionRequest;
import com.example.healthcareappointmentsystem.entity.Doctor;
import com.example.healthcareappointmentsystem.exception.ResourceNotFoundException;
import com.example.healthcareappointmentsystem.repository.DoctorRepository;
import com.example.healthcareappointmentsystem.repository.PrescriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;
/**
 * Service class for managing prescriptions.
 */
@Service
@RequiredArgsConstructor
public class PrescriptionService {
    private final PrescriptionRepository prescriptionRepository;
    private final MedicalRecordService medicalRecordService;
    private final DoctorRepository  doctorRepository;
    /**
     *Creates a new prescription and connects it with the signed in doctorm a specific patient and an appointment
     * @param request the prescription creation request containing patient idm appointment id, diagnisis, notes, and midicine
     * @return the new prescription
     */
    @LogOperation("CREATE_PRESCRIPTION")
    public Prescription createPrescription(CreatePrescriptionRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Doctor doctor = doctorRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", email));
        Prescription prescription = new Prescription();
        prescription.setPatientId(request.getPatientId());
        prescription.setDoctorId(doctor.getId());
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