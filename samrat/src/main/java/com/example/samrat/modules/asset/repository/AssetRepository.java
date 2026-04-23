package com.example.samrat.modules.asset.repository;

import com.example.samrat.modules.asset.entity.Asset;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {
    @Query("SELECT a FROM Asset a WHERE a.hospitalId = :hospitalId AND a.branchId = :branchId " +
           "AND (:categoryId IS NULL OR a.category.id = :categoryId) " +
           "AND (:status IS NULL OR a.status = :status) " +
           "AND (:keyword IS NULL OR LOWER(a.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(a.assetCode) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Asset> searchAssets(
            @Param("hospitalId") Long hospitalId,
            @Param("branchId") Long branchId,
            @Param("categoryId") Long categoryId,
            @Param("status") String status,
            @Param("keyword") String keyword,
            Pageable pageable);
}
