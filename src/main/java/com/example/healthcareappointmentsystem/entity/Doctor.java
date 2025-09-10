package com.example.healthcareappointmentsystem.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 * Represents a doctor in the system, extending the base User class.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Cacheable
@Table(name = "doctors")
public class Doctor extends User {
    @Column(nullable = false)
    private String specialty;
    private String phoneNumber;
    @Column(nullable = false)
    private boolean deleted = false;
}