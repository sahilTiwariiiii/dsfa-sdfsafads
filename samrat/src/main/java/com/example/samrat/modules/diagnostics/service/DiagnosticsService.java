package com.example.samrat.modules.diagnostics.service;

import com.example.samrat.core.context.TenantContext;
import com.example.samrat.modules.diagnostics.lab.entity.LabOrder;
import com.example.samrat.modules.diagnostics.lab.repository.LabOrderRepository;
import com.example.samrat.modules.doctor.entity.Doctor;
import com.example.samrat.modules.doctor.repository.DoctorRepository;
import com.example.samrat.modules.patient.entity.Patient;
import com.example.samrat.modules.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DiagnosticsService {

    private final LabOrderRepository labOrderRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    @Transactional
    public LabOrder createLabOrder(Long patientId, Long doctorId, String testName, String remarks) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        LabOrder order = new LabOrder();
        order.setPatient(patient);
        order.setOrderingDoctor(doctor);
        order.setTestName(testName);
        order.setTestCode("LAB-" + System.currentTimeMillis()); // Generating a dummy code
        order.setRemarks(remarks);
        order.setOrderTime(LocalDateTime.now());
        order.setStatus(LabOrder.LabStatus.ORDERED);
        order.setHospitalId(TenantContext.getHospitalId());
        order.setBranchId(TenantContext.getBranchId());

        return labOrderRepository.save(order);
    }

    @Transactional
    public LabOrder updateLabResult(Long orderId, String result, String remarks) {
        LabOrder order = labOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Lab order not found"));

        order.setResult(result);
        order.setRemarks(remarks);
        order.setStatus(LabOrder.LabStatus.COMPLETED);
        order.setResultTime(LocalDateTime.now());

        return labOrderRepository.save(order);
    }
}
