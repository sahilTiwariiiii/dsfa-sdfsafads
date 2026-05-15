package com.example.samrat.modules.inventory.cssd.repository;

import com.example.samrat.modules.inventory.cssd.entity.CSSDCycle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CSSDCycleRepository extends JpaRepository<CSSDCycle, Long> {
    Optional<CSSDCycle> findByCycleNumber(String cycleNumber);
}
