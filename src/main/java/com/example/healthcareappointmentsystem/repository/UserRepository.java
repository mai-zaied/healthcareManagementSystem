package com.example.healthcareappointmentsystem.repository;
import com.example.healthcareappointmentsystem.entity.User;
import com.example.healthcareappointmentsystem.security.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
/**
 * Repository interface for accessing User data.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByRole(Role role);
    boolean existsByEmail(String email);
}