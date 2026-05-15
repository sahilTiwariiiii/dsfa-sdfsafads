package com.example.samrat.modules.support.ambulance.repository;

import com.example.samrat.modules.support.ambulance.entity.Ambulance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AmbulanceRepository extends JpaRepository<Ambulance, Long> {
    Optional<Ambulance> findByRegistrationNumber(String registrationNumber);
}
