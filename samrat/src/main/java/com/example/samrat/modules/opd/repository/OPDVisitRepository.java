package com.example.samrat.modules.opd.repository;

import com.example.samrat.modules.opd.entity.OPDVisit;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OPDVisitRepository extends JpaRepository<OPDVisit, Long> {
    List<OPDVisit> findByHospitalIdAndBranchId(Long hospitalId, Long branchId);
}
