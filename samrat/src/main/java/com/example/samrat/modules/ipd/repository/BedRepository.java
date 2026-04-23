package com.example.samrat.modules.ipd.repository;

import com.example.samrat.modules.ipd.entity.Bed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BedRepository extends JpaRepository<Bed, Long> {
    List<Bed> findByHospitalIdAndBranchIdAndStatus(Long hospitalId, Long branchId, Bed.BedStatus status);
    Page<Bed> findByHospitalIdAndBranchId(Long hospitalId, Long branchId, Pageable pageable);
    List<Bed> findByWardId(Long wardId);
    List<Bed> findByWardIdAndStatus(Long wardId, Bed.BedStatus status);
}
