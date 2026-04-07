package com.example.samrat.modules.clinical.emr.repository;

import com.example.samrat.modules.clinical.emr.entity.EMRRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EMRRepository extends JpaRepository<EMRRecord, Long> {
    List<EMRRecord> findByPatientId(Long patientId);
}
