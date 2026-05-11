package com.example.samrat.modules.admin.controller;

import com.example.samrat.core.dto.BaseResponse;
import com.example.samrat.modules.admin.dto.DepartmentCreateRequest;
import com.example.samrat.modules.admin.entity.Department;
import com.example.samrat.modules.admin.service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
@Tag(name = "Department Management", description = "APIs for hospital department administration")
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN_WRITE')")
    @Operation(summary = "Create department", description = "Adds a new department to the hospital branch. Only `name` is required; `code` is auto-generated if omitted.")
    public ResponseEntity<BaseResponse<Department>> createDepartment(@Valid @RequestBody DepartmentCreateRequest request) {
        Department createdDepartment = departmentService.createDepartment(request);
        return ResponseEntity.ok(new BaseResponse<>(true, "Department created successfully", null, createdDepartment));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('DEPARTMENT_READ')")
    @Operation(summary = "Method Summary", description = "Retrieves all departments available in the current hospital branch with pagination")
    public ResponseEntity<BaseResponse<Page<Department>>> getAllDepartments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Department> departments = departmentService.getAllDepartments(pageable);
        return ResponseEntity.ok(new BaseResponse<>(true, "Departments found", null, departments));
    }

    @GetMapping("/list-all")
    @PreAuthorize("hasAuthority('DEPARTMENT_READ')")
    @Operation(summary = "List all departments (no pagination)", description = "Retrieves a simple list of all departments")
    public ResponseEntity<BaseResponse<List<Department>>> listAllDepartments() {
        List<Department> departments = departmentService.getAllDepartmentsInBranch();
        return ResponseEntity.ok(new BaseResponse<>(true, "Departments list found", null, departments));
    }

    @GetMapping("/active")
    @PreAuthorize("hasAuthority('DEPARTMENT_READ')")
    @Operation(summary = "Method Summary", description = "Retrieves all active departments")
    public ResponseEntity<BaseResponse<List<Department>>> getActiveDepartments() {
        List<Department> departments = departmentService.getActiveDepartments();
        return ResponseEntity.ok(new BaseResponse<>(true, "Active departments found", null, departments));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('DEPARTMENT_READ')")
    @Operation(summary = "Method Summary", description = "Retrieves a department by ID")
    public ResponseEntity<BaseResponse<Department>> getDepartmentById(@PathVariable Long id) {
        Department department = departmentService.getDepartmentById(id);
        return ResponseEntity.ok(new BaseResponse<>(true, "Department found", null, department));
    }

    @GetMapping("/code/{code}")
    @PreAuthorize("hasAuthority('DEPARTMENT_READ')")
    @Operation(summary = "Method Summary", description = "Retrieves a department by its unique code")
    public ResponseEntity<BaseResponse<Department>> getDepartmentByCode(@PathVariable String code) {
        Department department = departmentService.getDepartmentByCode(code);
        return ResponseEntity.ok(new BaseResponse<>(true, "Department found", null, department));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('DEPARTMENT_READ')")
    @Operation(summary = "Search departments", description = "Search departments by name or code")
    public ResponseEntity<BaseResponse<Page<Department>>> searchDepartments(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Department> departments = departmentService.searchDepartments(query, pageable);
        return ResponseEntity.ok(new BaseResponse<>(true, "Search results found", null, departments));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN_WRITE')")
    @Operation(summary = "Update department", description = "Updates an existing department")
    public ResponseEntity<BaseResponse<Department>> updateDepartment(@PathVariable Long id, @RequestBody Department department) {
        Department updatedDepartment = departmentService.updateDepartment(id, department);
        return ResponseEntity.ok(new BaseResponse<>(true, "Department updated successfully", null, updatedDepartment));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN_WRITE')")
    @Operation(summary = "Method Summary", description = "Deletes a department")
    public ResponseEntity<BaseResponse<Void>> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.ok(new BaseResponse<>(true, "Department deleted successfully", null, null));
    }
}

