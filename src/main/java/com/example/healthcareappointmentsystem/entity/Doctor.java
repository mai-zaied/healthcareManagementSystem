package com.example.healthcareappointmentsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


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
}