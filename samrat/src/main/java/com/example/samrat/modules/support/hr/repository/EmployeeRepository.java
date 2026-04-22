package com.example.samrat.modules.support.hr.repository;

import com.example.samrat.modules.support.hr.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByHospitalIdAndBranchId(Long hospitalId, Long branchId);
}
