package com.example.healthcareappointmentsystem.collection;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;
/**
 * Represents a prescription issued to a patient by a doctor
 * Linked to a patient, doctor, and appointment with their IDs
 */

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
    private Long appointmentId;

    private String diagnosis;
    private String notes;
    private List<String> medicines;
    private LocalDateTime date = LocalDateTime.now();

    @Override
    public String toString() {
        return String.format("Prescription[id=%s, patientId=%d, doctorId=%d, medicines=%s]",
                id, patientId, doctorId, medicines);
    }
}
