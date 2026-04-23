package com.example.samrat.modules.billing.repository;

import com.example.samrat.modules.billing.entity.ServiceCharge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ServiceChargeRepository extends JpaRepository<ServiceCharge, Long> {
    Page<ServiceCharge> findByHospitalIdAndBranchId(Long hospitalId, Long branchId, Pageable pageable);
    List<ServiceCharge> findByCategory(String category);
    List<ServiceCharge> findByNameContainingIgnoreCase(String name);
}
