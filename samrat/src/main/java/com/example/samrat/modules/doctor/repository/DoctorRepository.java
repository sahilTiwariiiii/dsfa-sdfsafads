package com.example.samrat.modules.doctor.repository;

import com.example.samrat.modules.doctor.entity.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    @Query("SELECT d FROM Doctor d JOIN d.branches b WHERE b.id = :branchId AND d.hospitalId = :hospitalId AND d.available = true")
    List<Doctor> findAvailableDoctorsByBranch(@Param("hospitalId") Long hospitalId, @Param("branchId") Long branchId);

    List<Doctor> findByHospitalIdAndSpecialization(Long hospitalId, String specialization);

    List<Doctor> findByDepartmentId(Long departmentId);

    @Query("SELECT d FROM Doctor d WHERE d.hospitalId = :hospitalId " +
           "AND (:departmentId IS NULL OR d.department.id = :departmentId) " +
           "AND (:query IS NULL OR " +
           "LOWER(d.user.fullName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(d.specialization) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Doctor> searchDoctors(@Param("hospitalId") Long hospitalId, @Param("departmentId") Long departmentId, @Param("query") String query, Pageable pageable);

    Page<Doctor> findByHospitalId(Long hospitalId, Pageable pageable);
}
