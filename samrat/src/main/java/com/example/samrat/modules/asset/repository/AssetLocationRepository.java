package com.example.samrat.modules.asset.repository;

import com.example.samrat.modules.asset.entity.AssetLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetLocationRepository extends JpaRepository<AssetLocation, Long> {
}
