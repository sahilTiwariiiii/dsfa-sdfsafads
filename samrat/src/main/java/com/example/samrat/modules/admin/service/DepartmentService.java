package com.example.samrat.modules.admin.service;

import com.example.samrat.core.context.TenantContext;
import com.example.samrat.modules.admin.entity.Department;
import com.example.samrat.modules.admin.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Transactional
    public Department createDepartment(Department department) {
        department.setHospitalId(TenantContext.getHospitalId());
        department.setBranchId(TenantContext.getBranchId());
        return departmentRepository.save(department);
    }

    public List<Department> getAllDepartmentsInBranch() {
        return departmentRepository.findByHospitalIdAndBranchId(
                TenantContext.getHospitalId(), TenantContext.getBranchId());
    }

    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));
    }
}
