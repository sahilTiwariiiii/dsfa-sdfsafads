package com.example.samrat.modules.admin.controller;

import com.example.samrat.core.dto.BaseResponse;
import com.example.samrat.modules.admin.entity.Department;
import com.example.samrat.modules.admin.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN_WRITE')")
    public ResponseEntity<BaseResponse<Department>> createDepartment(@RequestBody Department department) {
        Department createdDepartment = departmentService.createDepartment(department);
        return ResponseEntity.ok(new BaseResponse<>(true, "Department created successfully", null, createdDepartment));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('DEPARTMENT_READ')")
    public ResponseEntity<BaseResponse<List<Department>>> getAllDepartments() {
        List<Department> departments = departmentService.getAllDepartmentsInBranch();
        return ResponseEntity.ok(new BaseResponse<>(true, "Departments found", null, departments));
    }
}
