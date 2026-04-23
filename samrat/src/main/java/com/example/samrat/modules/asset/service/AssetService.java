package com.example.samrat.modules.asset.service;

import com.example.samrat.core.context.TenantContext;
import com.example.samrat.modules.asset.entity.*;
import com.example.samrat.modules.asset.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssetService {

    private final AssetRepository assetRepository;
    private final AssetCategoryRepository categoryRepository;
    private final AssetSubCategoryRepository subCategoryRepository;
    private final AssetVendorRepository vendorRepository;
    private final AssetLocationRepository locationRepository;
    private final AssetAssignmentRepository assignmentRepository;
    private final AssetMaintenanceRepository maintenanceRepository;

    // --- Assets ---
    @Transactional
    public Asset createAsset(Asset asset) {
        asset.setHospitalId(TenantContext.getHospitalId());
        asset.setBranchId(TenantContext.getBranchId());
        return assetRepository.save(asset);
    }

    public Page<Asset> searchAssets(Long categoryId, String status, String keyword, Pageable pageable) {
        return assetRepository.searchAssets(TenantContext.getHospitalId(), TenantContext.getBranchId(), categoryId, status, keyword, pageable);
    }

    public Asset getAssetById(Long id) {
        return assetRepository.findById(id).orElseThrow(() -> new RuntimeException("Asset not found"));
    }

    @Transactional
    public Asset updateAsset(Long id, Asset details) {
        Asset asset = getAssetById(id);
        asset.setName(details.getName());
        asset.setStatus(details.getStatus());
        asset.setDepartment(details.getDepartment());
        // ... update other fields
        return assetRepository.save(asset);
    }

    // --- Categories ---
    @Transactional
    public AssetCategory createCategory(AssetCategory category) {
        category.setHospitalId(TenantContext.getHospitalId());
        category.setBranchId(TenantContext.getBranchId());
        return categoryRepository.save(category);
    }

    public List<AssetCategory> getAllCategories() {
        return categoryRepository.findAll();
    }

    // --- Vendors ---
    @Transactional
    public AssetVendor createVendor(AssetVendor vendor) {
        vendor.setHospitalId(TenantContext.getHospitalId());
        vendor.setBranchId(TenantContext.getBranchId());
        return vendorRepository.save(vendor);
    }

    public List<AssetVendor> getAllVendors() {
        return vendorRepository.findAll();
    }

    // --- Locations ---
    @Transactional
    public AssetLocation createLocation(AssetLocation location) {
        location.setHospitalId(TenantContext.getHospitalId());
        location.setBranchId(TenantContext.getBranchId());
        return locationRepository.save(location);
    }

    public List<AssetLocation> getAllLocations() {
        return locationRepository.findAll();
    }

    // --- Assignments ---
    @Transactional
    public AssetAssignment assignAsset(AssetAssignment assignment) {
        Asset asset = getAssetById(assignment.getAsset().getId());
        asset.setStatus("ASSIGNED");
        assetRepository.save(asset);

        assignment.setHospitalId(TenantContext.getHospitalId());
        assignment.setBranchId(TenantContext.getBranchId());
        return assignmentRepository.save(assignment);
    }

    // --- Maintenance ---
    @Transactional
    public AssetMaintenance scheduleMaintenance(AssetMaintenance maintenance) {
        Asset asset = getAssetById(maintenance.getAsset().getId());
        asset.setStatus("UNDER_MAINTENANCE");
        assetRepository.save(asset);

        maintenance.setHospitalId(TenantContext.getHospitalId());
        maintenance.setBranchId(TenantContext.getBranchId());
        return maintenanceRepository.save(maintenance);
    }
}
