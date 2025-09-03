package com.example.healthcareappointmentsystem.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "prescriptions")
public class Prescription {

    @Id
    private String id;

    private Long patientId;
    private Long doctorId;

    private String notes;
    private List<String> medicines;
    private LocalDateTime date = LocalDateTime.now();
}
