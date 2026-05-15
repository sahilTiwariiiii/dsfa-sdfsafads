package com.example.samrat.modules.support.diet.repository;

import com.example.samrat.modules.support.diet.entity.DietPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface DietPlanRepository extends JpaRepository<DietPlan, Long> {
    Optional<DietPlan> findByAdmissionId(Long admissionId);
}
