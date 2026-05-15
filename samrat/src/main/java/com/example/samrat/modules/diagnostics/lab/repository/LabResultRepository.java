package com.example.samrat.modules.diagnostics.lab.repository;

import com.example.samrat.modules.diagnostics.lab.entity.LabResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LabResultRepository extends JpaRepository<LabResult, Long> {
}
