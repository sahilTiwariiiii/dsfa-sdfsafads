package com.example.samrat.modules.reporting.repository;

import com.example.samrat.modules.reporting.entity.SystemReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SystemReportRepository extends JpaRepository<SystemReport, Long> {
    List<SystemReport> findByHospitalIdAndBranchId(Long hospitalId, Long branchId);
}
