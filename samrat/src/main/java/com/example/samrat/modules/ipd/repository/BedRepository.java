package com.example.samrat.modules.ipd.repository;

import com.example.samrat.modules.ipd.entity.Bed;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BedRepository extends JpaRepository<Bed, Long> {
    List<Bed> findByHospitalIdAndBranchIdAndStatus(Long hospitalId, Long branchId, Bed.BedStatus status);
}
