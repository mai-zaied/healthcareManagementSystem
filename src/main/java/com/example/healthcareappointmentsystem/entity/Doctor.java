package com.example.healthcareappointmentsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "doctors")
public class Doctor extends User {

    @Column(nullable = false)
    private String specialty;

    private LocalTime workStart = LocalTime.of(8, 0);
    private LocalTime workEnd = LocalTime.of(17, 0);
}
