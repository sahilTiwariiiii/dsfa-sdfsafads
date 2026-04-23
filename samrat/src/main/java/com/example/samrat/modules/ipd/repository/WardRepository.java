package com.example.samrat.modules.ipd.repository;

import com.example.samrat.modules.ipd.entity.Ward;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WardRepository extends JpaRepository<Ward, Long> {
    List<Ward> findByHospitalIdAndBranchId(Long hospitalId, Long branchId);
    Page<Ward> findByHospitalIdAndBranchId(Long hospitalId, Long branchId, Pageable pageable);
    List<Ward> findByDepartmentId(Long departmentId);
}
