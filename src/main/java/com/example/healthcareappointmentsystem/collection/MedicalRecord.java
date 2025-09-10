package com.example.healthcareappointmentsystem.collection;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;
/**
 * Stores medical information for a patient
 * Linked to a patient with patientId
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "medical_records")
public class MedicalRecord {
    @Id
    private String id;
    private Long patientId;
    private List<String> allergies;
    private List<String> chronicConditions;
    private List<String> prescriptionIds;
    private List<String> labResults;
}