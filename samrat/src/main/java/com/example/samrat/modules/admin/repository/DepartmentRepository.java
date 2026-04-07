package com.example.samrat.modules.admin.repository;

import com.example.samrat.modules.admin.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    List<Department> findByHospitalIdAndBranchId(Long hospitalId, Long branchId);
}
