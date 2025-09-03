package com.example.healthcareappointmentsystem.repository;

import com.example.healthcareappointmentsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

}
