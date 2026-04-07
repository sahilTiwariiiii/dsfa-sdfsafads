package com.example.samrat.modules.diagnostics.lab.repository;

import com.example.samrat.modules.diagnostics.lab.entity.LabOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LabOrderRepository extends JpaRepository<LabOrder, Long> {
    List<LabOrder> findByPatientId(Long patientId);
    List<LabOrder> findByStatus(LabOrder.LabStatus status);
}
