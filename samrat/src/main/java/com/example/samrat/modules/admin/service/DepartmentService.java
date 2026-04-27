package com.example.samrat.modules.admin.service;

import com.example.samrat.core.context.TenantContext;
import com.example.samrat.modules.admin.dto.DepartmentCreateRequest;
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
    public Department createDepartment(DepartmentCreateRequest request) {
        Department department = new Department();
        department.setHospitalId(TenantContext.getHospitalId());
        department.setBranchId(TenantContext.getBranchId());

        department.setName(request.getName());
        department.setCode(resolveDepartmentCode(request.getCode(), request.getName()));
        department.setDescription(request.getDescription());
        department.setActive(true);
        department.setHeadOfDepartment(request.getHeadOfDepartment());
        department.setLocation(request.getLocation());
        department.setContactNumber(request.getContactNumber());
        department.setEmail(request.getEmail());
        department.setTotalBeds(request.getTotalBeds());
        department.setAvailableBeds(request.getAvailableBeds());

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
        if (departmentDetails.getCode() != null && !departmentDetails.getCode().isBlank()) {
            department.setCode(departmentDetails.getCode());
        }
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

    private String resolveDepartmentCode(String requestedCode, String name) {
        String base = (requestedCode != null && !requestedCode.isBlank())
                ? requestedCode.trim()
                : slugToCode(name);

        String hospitalId = String.valueOf(TenantContext.getHospitalId());
        String branchId = String.valueOf(TenantContext.getBranchId());

        // ensure uniqueness within tenant+branch
        String candidate = base;
        int attempt = 1;
        while (departmentRepository.existsByHospitalIdAndBranchIdAndCode(TenantContext.getHospitalId(), TenantContext.getBranchId(), candidate)) {
            candidate = base + "-" + attempt;
            attempt++;
        }

        // also avoid accidental empty codes
        if (candidate.isBlank()) {
            candidate = "DEPT-" + hospitalId + "-" + branchId;
        }
        return candidate;
    }

    private String slugToCode(String input) {
        if (input == null) return "DEPT";
        String cleaned = input.trim().toUpperCase().replaceAll("[^A-Z0-9]+", "-");
        cleaned = cleaned.replaceAll("(^-+|-+$)", "");
        if (cleaned.isBlank()) return "DEPT";
        // keep codes short-ish
        return cleaned.length() > 16 ? cleaned.substring(0, 16) : cleaned;
    }
}
