package com.example.samrat.modules.clinical.discharge.repository;

import com.example.samrat.modules.clinical.discharge.entity.DischargeSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DischargeSummaryRepository extends JpaRepository<DischargeSummary, Long> {
    Optional<DischargeSummary> findByAdmissionId(Long admissionId);
    Page<DischargeSummary> findByHospitalIdAndBranchId(Long hospitalId, Long branchId, Pageable pageable);
}
