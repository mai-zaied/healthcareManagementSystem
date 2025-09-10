package com.example.healthcareappointmentsystem.repository;
import com.example.healthcareappointmentsystem.entity.DoctorSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
/**
 * Repository interface for managing DoctorSchedule data.
 */
@Repository
public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, Long> {
    @Query("SELECT ds FROM DoctorSchedule ds WHERE " +
            "ds.doctor.id = :doctorId AND " +
            "ds.date = :date AND " +
            ":startTime >= ds.startTime AND " +
            ":endTime <= ds.endTime")
    Optional<DoctorSchedule> isTimeSlotAvailable(
            @Param("doctorId") Long doctorId,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime);
    List<DoctorSchedule> findByDoctorId(Long doctorId);
    List<DoctorSchedule> findByDoctorIdAndDate(Long doctorId, LocalDate date);
    void deleteByDoctorId(Long doctorId);
}