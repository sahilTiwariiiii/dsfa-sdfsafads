package com.example.samrat.modules.support.service;

import com.example.samrat.core.context.TenantContext;
import com.example.samrat.modules.support.hr.entity.Employee;
import com.example.samrat.modules.support.hr.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SupportService {

    private final EmployeeRepository employeeRepository;

    @Transactional
    public Employee hireEmployee(Employee employee) {
        employee.setHospitalId(TenantContext.getHospitalId());
        employee.setBranchId(TenantContext.getBranchId());
        return employeeRepository.save(employee);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findByHospitalIdAndBranchId(
                TenantContext.getHospitalId(), TenantContext.getBranchId());
    }
}
