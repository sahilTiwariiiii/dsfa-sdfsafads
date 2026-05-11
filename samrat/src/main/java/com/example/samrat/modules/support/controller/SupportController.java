package com.example.samrat.modules.support.controller;

import com.example.samrat.core.dto.BaseResponse;
import com.example.samrat.modules.support.hr.entity.Employee;
import com.example.samrat.modules.support.service.SupportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/support")
@RequiredArgsConstructor
@Tag(name = "Support Management", description = "APIs for HR, employee management, and hospital support services")
public class SupportController {

    private final SupportService supportService;

    @PostMapping("/employee")
    @PreAuthorize("hasAuthority('HR_WRITE')")
    @Operation(summary = "Hire employee", description = "Registers a new hospital employee in the system")
    public ResponseEntity<BaseResponse<Employee>> hireEmployee(@RequestBody Employee employee) {
        Employee createdEmployee = supportService.hireEmployee(employee);
        return ResponseEntity.ok(new BaseResponse<>(true, "Employee hired successfully", null, createdEmployee));
    }

    @GetMapping("/employees")
    @PreAuthorize("hasAuthority('HR_READ')")
    @Operation(summary = "Method Summary", description = "Retrieves all current employees in the hospital")
    public ResponseEntity<BaseResponse<List<Employee>>> getAllEmployees() {
        List<Employee> employees = supportService.getAllEmployees();
        return ResponseEntity.ok(new BaseResponse<>(true, "Employees found", null, employees));
    }
}

