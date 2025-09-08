package com.example.healthcareappointmentsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import com.example.healthcareappointmentsystem.security.Role;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cache;


/**
 * This is a base class stores data for all system users
 * <p>Inheritance strategy: JOINED,
 * each subclass has its own table sharing the same primary key
 */

@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "users")
public class User {
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
