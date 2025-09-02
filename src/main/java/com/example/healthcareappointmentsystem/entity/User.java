package com.example.healthcareappointmentsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.example.healthcareappointmentsystem.security.Role;

/**
 * This is an abstract base class stores data for all system users
 * <p>Inheritance strategy: JOINED,
 * each subclass has its own table sharing the same primary key
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "users")

public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String fullName;

    @Enumerated(EnumType.STRING) //tells the database to store the Enum as string
    @Column(nullable = false)
    private Role role;
}
