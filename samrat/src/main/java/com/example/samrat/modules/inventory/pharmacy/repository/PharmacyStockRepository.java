package com.example.samrat.modules.inventory.pharmacy.repository;

import com.example.samrat.modules.inventory.pharmacy.entity.PharmacyStock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PharmacyStockRepository extends JpaRepository<PharmacyStock, Long> {
    Optional<PharmacyStock> findByBatchNumber(String batchNumber);
    Page<PharmacyStock> findByMedicineNameContainingIgnoreCase(String name, Pageable pageable);
    Page<PharmacyStock> findByHospitalIdAndBranchId(Long hospitalId, Long branchId, Pageable pageable);

    @Query("SELECT p FROM PharmacyStock p WHERE p.hospitalId = :hospitalId AND p.branchId = :branchId " +
           "AND (:name IS NULL OR LOWER(p.medicineName) LIKE LOWER(CONCAT('%', :name, '%'))) " +
           "AND (:genericName IS NULL OR LOWER(p.genericName) LIKE LOWER(CONCAT('%', :genericName, '%'))) " +
           "AND (:batchNumber IS NULL OR p.batchNumber = :batchNumber)")
    Page<PharmacyStock> searchStock(
            @Param("hospitalId") Long hospitalId,
            @Param("branchId") Long branchId,
            @Param("name") String name,
            @Param("genericName") String genericName,
            @Param("batchNumber") String batchNumber,
            Pageable pageable);
}
