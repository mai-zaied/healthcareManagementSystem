package com.example.healthcareappointmentsystem.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "medical_records")
public class MedicalRecord {

    @Id
    private String id;
    private Long patientId;

    private List<Prescription> prescriptions;
    private List<String> labResults;
}
