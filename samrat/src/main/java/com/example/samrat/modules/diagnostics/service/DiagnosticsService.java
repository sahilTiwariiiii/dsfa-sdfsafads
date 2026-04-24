package com.example.samrat.modules.diagnostics.service;

import com.example.samrat.core.context.TenantContext;
import com.example.samrat.modules.admin.repository.DepartmentRepository;
import com.example.samrat.modules.diagnostics.lab.entity.LabOrder;
import com.example.samrat.modules.diagnostics.lab.repository.LabOrderRepository;
import com.example.samrat.modules.diagnostics.radiology.entity.RadiologyOrder;
import com.example.samrat.modules.diagnostics.radiology.repository.RadiologyOrderRepository;
import com.example.samrat.modules.doctor.entity.Doctor;
import com.example.samrat.modules.doctor.repository.DoctorRepository;
import com.example.samrat.modules.patient.entity.Patient;
import com.example.samrat.modules.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DiagnosticsService {

    private final LabOrderRepository labOrderRepository;
    private final RadiologyOrderRepository radiologyOrderRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;

    // --- Lab Management ---

    @Transactional
    public LabOrder createLabOrder(LabOrder order, Long patientId, Long doctorId, Long departmentId) {
        order.setPatient(patientRepository.findById(patientId).orElseThrow(() -> new RuntimeException("Patient not found")));
        order.setOrderingDoctor(doctorRepository.findById(doctorId).orElseThrow(() -> new RuntimeException("Doctor not found")));
        if (departmentId != null) {
            order.setDepartment(departmentRepository.findById(departmentId).orElseThrow(() -> new RuntimeException("Department not found")));
        }
        order.setOrderNumber("LAB-" + System.currentTimeMillis() % 1000000);
        order.setOrderTime(LocalDateTime.now());
        order.setStatus(LabOrder.LabStatus.ORDERED);
        order.setHospitalId(TenantContext.getHospitalId());
        order.setBranchId(TenantContext.getBranchId());
        return labOrderRepository.save(order);
    }

    public Page<LabOrder> searchLabOrders(Long patientId, Long doctorId, String status, LocalDateTime start, LocalDateTime end, Pageable pageable) {
        LabOrder.LabStatus labStatus = status != null ? LabOrder.LabStatus.valueOf(status.toUpperCase()) : null;
        return labOrderRepository.searchLabOrders(TenantContext.getHospitalId(), TenantContext.getBranchId(), patientId, doctorId, labStatus, start, end, pageable);
    }

    public LabOrder getLabOrderById(Long id) {
        return labOrderRepository.findById(id).orElseThrow(() -> new RuntimeException("Lab order not found"));
    }

    @Transactional
    public void deleteLabOrder(Long id) {
        labOrderRepository.deleteById(id);
    }

    @Transactional
    public LabOrder updateLabResult(Long orderId, String result, String remarks) {
        LabOrder order = labOrderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Lab order not found"));
        order.setResult(result);
        order.setRemarks(remarks);
        order.setStatus(LabOrder.LabStatus.COMPLETED);
        order.setResultTime(LocalDateTime.now());
        return labOrderRepository.save(order);
    }

    // --- Radiology Management ---

    @Transactional
    public RadiologyOrder createRadiologyOrder(RadiologyOrder order, Long patientId, Long doctorId, Long departmentId) {
        order.setPatient(patientRepository.findById(patientId).orElseThrow(() -> new RuntimeException("Patient not found")));
        order.setOrderingDoctor(doctorRepository.findById(doctorId).orElseThrow(() -> new RuntimeException("Doctor not found")));
        if (departmentId != null) {
            order.setDepartment(departmentRepository.findById(departmentId).orElseThrow(() -> new RuntimeException("Department not found")));
        }
        order.setOrderNumber("RAD-" + System.currentTimeMillis() % 1000000);
        order.setOrderTime(LocalDateTime.now());
        order.setStatus(RadiologyOrder.RadiologyStatus.ORDERED);
        order.setHospitalId(TenantContext.getHospitalId());
        order.setBranchId(TenantContext.getBranchId());
        return radiologyOrderRepository.save(order);
    }

    public Page<RadiologyOrder> searchRadiologyOrders(Long patientId, Long doctorId, String status, LocalDateTime start, LocalDateTime end, Pageable pageable) {
        RadiologyOrder.RadiologyStatus radStatus = status != null ? RadiologyOrder.RadiologyStatus.valueOf(status.toUpperCase()) : null;
        return radiologyOrderRepository.searchRadiologyOrders(TenantContext.getHospitalId(), TenantContext.getBranchId(), patientId, doctorId, radStatus, start, end, pageable);
    }

    public RadiologyOrder getRadiologyOrderById(Long id) {
        return radiologyOrderRepository.findById(id).orElseThrow(() -> new RuntimeException("Radiology order not found"));
    }

    @Transactional
    public void deleteRadiologyOrder(Long id) {
        radiologyOrderRepository.deleteById(id);
    }

    @Transactional
    public RadiologyOrder updateRadiologyReport(Long orderId, String report, String impression) {
        RadiologyOrder order = radiologyOrderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Radiology order not found"));
        order.setReport(report);
        order.setImpression(impression);
        order.setStatus(RadiologyOrder.RadiologyStatus.COMPLETED);
        order.setReportTime(LocalDateTime.now());
        return radiologyOrderRepository.save(order);
    }
}
