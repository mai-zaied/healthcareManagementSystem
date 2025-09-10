package com.example.healthcareappointmentsystem.repository;

import com.example.healthcareappointmentsystem.entity.Appointment;
import com.example.healthcareappointmentsystem.entity.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    //overlap checking
    @Query(
            "SELECT CASE WHEN COUNT (a) > 0 THEN true ELSE false END FROM Appointment a WHERE " +
                    "a.doctor.id = :doctorId AND " +
                    "a.status <> 'CANCELLED' AND " +
                    "(:startTime < a.endTime AND :endTime > a.startTime)"
    )
    boolean existsOverlappingAppointment(
            @Param("doctorId") Long doctorId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    @Query(
            "SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Appointment a WHERE " +
                    "a.patient.id = :patientId AND " +
                    "a.status <> 'CANCELLED' AND " +
                    "(:startTime < a.endTime AND :endTime > a.startTime)"
    )
    boolean existsOverlappingPatientAppointment(
            @Param("patientId") Long patientId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    List<Appointment> findByPatientId(Long patientId);
    List<Appointment> findByDoctorId(Long doctorId);
    List<Appointment> findByDoctorIdAndStatus(Long doctorId, AppointmentStatus status);
}