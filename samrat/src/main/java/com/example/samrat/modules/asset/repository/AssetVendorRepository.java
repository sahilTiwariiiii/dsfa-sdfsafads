package com.example.samrat.modules.asset.repository;

import com.example.samrat.modules.asset.entity.AssetVendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetVendorRepository extends JpaRepository<AssetVendor, Long> {
}
