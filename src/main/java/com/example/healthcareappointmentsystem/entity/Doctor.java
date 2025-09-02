package com.example.healthcareappointmentsystem.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;

/**
 * Doctor entity extending the abstract User class.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "doctors")

public class Doctor extends User {
    @Column(nullable = false)
    private String specialty;


}
