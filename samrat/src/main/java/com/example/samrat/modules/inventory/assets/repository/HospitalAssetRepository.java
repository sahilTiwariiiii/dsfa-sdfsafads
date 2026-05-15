package com.example.samrat.modules.inventory.assets.repository;

import com.example.samrat.modules.inventory.assets.entity.HospitalAsset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface HospitalAssetRepository extends JpaRepository<HospitalAsset, Long> {
    Optional<HospitalAsset> findByAssetTag(String assetTag);
}
