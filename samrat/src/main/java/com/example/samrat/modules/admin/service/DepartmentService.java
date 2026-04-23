package com.example.samrat.modules.admin.service;

import com.example.samrat.core.context.TenantContext;
import com.example.samrat.modules.admin.entity.Department;
import com.example.samrat.modules.admin.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<Department> getAllDepartments(Pageable pageable) {
        return departmentRepository.findByHospitalIdAndBranchId(
                TenantContext.getHospitalId(), TenantContext.getBranchId(), pageable);
    }

    public List<Department> getActiveDepartments() {
        return departmentRepository.findByHospitalIdAndBranchIdAndActiveTrue(
                TenantContext.getHospitalId(), TenantContext.getBranchId());
    }

    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));
    }

    public Department getDepartmentByCode(String code) {
        return departmentRepository.findByHospitalIdAndBranchIdAndCode(
                TenantContext.getHospitalId(), TenantContext.getBranchId(), code)
                .orElseThrow(() -> new RuntimeException("Department not found with code: " + code));
    }

    public Page<Department> searchDepartments(String query, Pageable pageable) {
        return departmentRepository.searchDepartments(
                TenantContext.getHospitalId(), TenantContext.getBranchId(), query, pageable);
    }

    @Transactional
    public Department updateDepartment(Long id, Department departmentDetails) {
        Department department = getDepartmentById(id);
        department.setName(departmentDetails.getName());
        department.setCode(departmentDetails.getCode());
        department.setDescription(departmentDetails.getDescription());
        department.setActive(departmentDetails.isActive());
        department.setHeadOfDepartment(departmentDetails.getHeadOfDepartment());
        department.setLocation(departmentDetails.getLocation());
        department.setContactNumber(departmentDetails.getContactNumber());
        department.setEmail(departmentDetails.getEmail());
        department.setTotalBeds(departmentDetails.getTotalBeds());
        department.setAvailableBeds(departmentDetails.getAvailableBeds());
        return departmentRepository.save(department);
    }

    @Transactional
    public void deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
    }
}
