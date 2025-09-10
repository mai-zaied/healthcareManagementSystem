package com.example.healthcareappointmentsystem.repository;
import com.example.healthcareappointmentsystem.entity.Doctor;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
/**
 * Repository interface for accessing Doctor data in the database.
 * Extends JpaRepository to provide standard CRUD operations.
 * <p>Also provides custom queries for:
 *  - Finding doctors by specialty
 *  - Retrieving all unique specialties
 *  - Handling soft-deleted doctors</p>
 *  */
@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    @Query("SELECT d FROM Doctor d WHERE d.specialty = :specialty")
    @QueryHints({
            @QueryHint(name = "org.hibernate.cacheable", value = "true"),
            @QueryHint(name = "org.hibernate.cacheRegion", value = "doctorBySpecialtyQuery")
    })
    List<Doctor> findBySpecialty(String specialty);
    @Query("SELECT DISTINCT d.specialty FROM Doctor d")
    List<String> findDistinctSpecialty();
    boolean existsById(Long id);
    Optional<Doctor> findByEmail(String email);
    List<Doctor> findAllByDeletedFalse();
    Optional<Doctor> findByIdAndDeletedFalse(Long id);
}