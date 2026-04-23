package com.example.samrat.modules.clinical.emr.repository;

import com.example.samrat.modules.clinical.emr.entity.ClinicalDiagnosis;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClinicalDiagnosisRepository extends JpaRepository<ClinicalDiagnosis, Long> {
    Page<ClinicalDiagnosis> findByPatientId(Long patientId, Pageable pageable);
    Page<ClinicalDiagnosis> findByVisitId(Long visitId, Pageable pageable);
}
