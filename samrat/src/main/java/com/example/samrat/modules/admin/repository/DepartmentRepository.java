package com.example.samrat.modules.admin.repository;

import com.example.samrat.modules.admin.entity.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    List<Department> findByHospitalIdAndBranchId(Long hospitalId, Long branchId);

    Page<Department> findByHospitalIdAndBranchId(Long hospitalId, Long branchId, Pageable pageable);

    Optional<Department> findByHospitalIdAndBranchIdAndCode(Long hospitalId, Long branchId, String code);

    @Query("SELECT d FROM Department d WHERE d.hospitalId = :hospitalId AND d.branchId = :branchId " +
           "AND (:query IS NULL OR LOWER(d.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(d.code) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Department> searchDepartments(@Param("hospitalId") Long hospitalId, @Param("branchId") Long branchId, @Param("query") String query, Pageable pageable);

    List<Department> findByHospitalIdAndBranchIdAndActiveTrue(Long hospitalId, Long branchId);
}
