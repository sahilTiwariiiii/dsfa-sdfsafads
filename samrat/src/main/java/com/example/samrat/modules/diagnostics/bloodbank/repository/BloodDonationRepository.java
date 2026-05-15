package com.example.samrat.modules.diagnostics.bloodbank.repository;

import com.example.samrat.modules.diagnostics.bloodbank.entity.BloodDonation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface BloodDonationRepository extends JpaRepository<BloodDonation, Long> {
    Optional<BloodDonation> findByBagId(String bagId);
}
