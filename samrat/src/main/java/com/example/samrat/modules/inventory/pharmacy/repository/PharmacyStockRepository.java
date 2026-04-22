package com.example.samrat.modules.inventory.pharmacy.repository;

import com.example.samrat.modules.inventory.pharmacy.entity.PharmacyStock;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PharmacyStockRepository extends JpaRepository<PharmacyStock, Long> {
    Optional<PharmacyStock> findByBatchNumber(String batchNumber);
    List<PharmacyStock> findByMedicineNameContaining(String name);
    List<PharmacyStock> findByHospitalIdAndBranchId(Long hospitalId, Long branchId);
}
