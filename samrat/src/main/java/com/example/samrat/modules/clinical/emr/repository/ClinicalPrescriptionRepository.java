package com.example.samrat.modules.clinical.emr.repository;

import com.example.samrat.modules.clinical.emr.entity.ClinicalPrescription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClinicalPrescriptionRepository extends JpaRepository<ClinicalPrescription, Long> {
    Page<ClinicalPrescription> findByPatientId(Long patientId, Pageable pageable);
    Page<ClinicalPrescription> findByVisitId(Long visitId, Pageable pageable);
}
