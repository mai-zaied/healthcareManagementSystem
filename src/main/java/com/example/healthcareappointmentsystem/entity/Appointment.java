package com.example.healthcareappointmentsystem.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
/**
 * Represents an appointment between a Doctor and a Patient.
 * Stores the start and end times, status, and ensures a unique time slot per doctor.
 * Each appointment is linked to one Doctor and one Patient.
 * The endTime is automatically calculated 30 minutes after startTime.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "appointments",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"doctor_id", "startTime"})
        })
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;
    @ManyToOne(optional = false)
    @JoinColumn(name = "patient_id")
    private Patient patient;
    @Column(nullable = false)
    private LocalDateTime startTime;
    @Column(nullable = false)
    private LocalDateTime endTime;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status = AppointmentStatus.SCHEDULED;

    @Override
    public String toString() {
        return String.format("Appointment[id=%d, doctor=%s, patient=%s, time=%s, status=%s]",
                id,  doctor.getFullName(), patient.getFullName(), startTime, status);
    }
}