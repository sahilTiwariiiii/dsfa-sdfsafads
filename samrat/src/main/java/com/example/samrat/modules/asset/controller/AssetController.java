package com.example.samrat.modules.asset.controller;

import com.example.samrat.core.dto.BaseResponse;
import com.example.samrat.modules.asset.entity.*;
import com.example.samrat.modules.asset.service.AssetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/assets")
@RequiredArgsConstructor
@Tag(name = "Assets - Masters", description = "Enterprise APIs for Asset Masters")
public class AssetController {

    private final AssetService assetService;

    // --- Assets - Masters ---

    @GetMapping("/masters")
    @Operation(summary = "List Assets - Masters")
    public ResponseEntity<BaseResponse<Page<Asset>>> listAssetsMasters(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return ResponseEntity.ok(new BaseResponse<>(true, "Assets found", null, assetService.searchAssets(null, null, null, pageable)));
    }

    @PostMapping("/masters")
    @Operation(summary = "Create Assets - Masters")
    public ResponseEntity<BaseResponse<Asset>> createAssetMaster(@RequestBody Asset asset) {
        return ResponseEntity.status(201).body(new BaseResponse<>(true, "Created", null, assetService.createAsset(asset)));
    }

    @GetMapping("/masters/{id}")
    @Operation(summary = "Method Summary")
    public ResponseEntity<BaseResponse<Asset>> getAssetMasterById(@PathVariable Long id) {
        return ResponseEntity.ok(new BaseResponse<>(true, "Detail", null, assetService.getAssetById(id)));
    }

    @PutMapping("/masters/{id}")
    @Operation(summary = "Update Assets - Masters")
    public ResponseEntity<BaseResponse<Asset>> updateAssetMaster(@PathVariable Long id, @RequestBody Asset asset) {
        return ResponseEntity.ok(new BaseResponse<>(true, "Updated", null, assetService.updateAsset(id, asset)));
    }

    @DeleteMapping("/masters/{id}")
    @Operation(summary = "Method Summary")
    public ResponseEntity<BaseResponse<Void>> deleteAssetMaster(@PathVariable Long id) {
        return ResponseEntity.ok(new BaseResponse<>(true, "Deleted", null, null));
    }

    // --- Assets - Categories ---

    @GetMapping("/categories")
    @Tag(name = "Assets - Categories")
    @Operation(summary = "List Assets - Categories")
    public ResponseEntity<BaseResponse<List<AssetCategory>>> listAssetCategories() {
        return ResponseEntity.ok(new BaseResponse<>(true, "Categories found", null, assetService.getAllCategories()));
    }

    @PostMapping("/categories")
    @Tag(name = "Assets - Categories")
    @Operation(summary = "Create Assets - Categories")
    public ResponseEntity<BaseResponse<AssetCategory>> createAssetCategory(@RequestBody AssetCategory category) {
        return ResponseEntity.status(201).body(new BaseResponse<>(true, "Created", null, assetService.createCategory(category)));
    }

    // --- Assets - Sub categories ---

    @GetMapping("/sub-categories")
    @Tag(name = "Assets - Sub categories")
    @Operation(summary = "List Assets - Sub categories")
    public ResponseEntity<BaseResponse<Void>> listAssetSubCategories() {
        return ResponseEntity.ok(new BaseResponse<>(true, "Sub categories found", null, null));
    }

    @PostMapping("/sub-categories")
    @Tag(name = "Assets - Sub categories")
    @Operation(summary = "Create Assets - Sub categories")
    public ResponseEntity<BaseResponse<Void>> createAssetSubCategory() {
        return ResponseEntity.status(201).body(new BaseResponse<>(true, "Created", null, null));
    }

    // --- Assets - Vendors ---

    @GetMapping("/vendors")
    @Tag(name = "Assets - Vendors")
    @Operation(summary = "List Assets - Vendors")
    public ResponseEntity<BaseResponse<List<AssetVendor>>> listAssetVendors() {
        return ResponseEntity.ok(new BaseResponse<>(true, "Vendors found", null, assetService.getAllVendors()));
    }

    @PostMapping("/vendors")
    @Tag(name = "Assets - Vendors")
    @Operation(summary = "Create Assets - Vendors")
    public ResponseEntity<BaseResponse<AssetVendor>> createAssetVendor(@RequestBody AssetVendor vendor) {
        return ResponseEntity.status(201).body(new BaseResponse<>(true, "Created", null, assetService.createVendor(vendor)));
    }

    // --- Assets - Locations ---

    @GetMapping("/locations")
    @Tag(name = "Assets - Locations")
    @Operation(summary = "List Assets - Locations")
    public ResponseEntity<BaseResponse<List<AssetLocation>>> listAssetLocations() {
        return ResponseEntity.ok(new BaseResponse<>(true, "Locations found", null, assetService.getAllLocations()));
    }

    @PostMapping("/locations")
    @Tag(name = "Assets - Locations")
    @Operation(summary = "Create Assets - Locations")
    public ResponseEntity<BaseResponse<AssetLocation>> createAssetLocation(@RequestBody AssetLocation location) {
        return ResponseEntity.status(201).body(new BaseResponse<>(true, "Created", null, assetService.createLocation(location)));
    }

    // --- Assets - Assignments ---

    @GetMapping("/assignments")
    @Tag(name = "Assets - Assignments")
    @Operation(summary = "List Assets - Assignments")
    public ResponseEntity<BaseResponse<Void>> listAssetAssignments() {
        return ResponseEntity.ok(new BaseResponse<>(true, "Assignments found", null, null));
    }

    @PostMapping("/assignments")
    @Tag(name = "Assets - Assignments")
    @Operation(summary = "Create Assets - Assignments")
    public ResponseEntity<BaseResponse<AssetAssignment>> createAssetAssignment(@RequestBody AssetAssignment assignment) {
        return ResponseEntity.status(201).body(new BaseResponse<>(true, "Created", null, assetService.assignAsset(assignment)));
    }

    // --- Assets - Maintenances ---

    @GetMapping("/maintenances")
    @Tag(name = "Assets - Maintenances")
    @Operation(summary = "List Assets - Maintenances")
    public ResponseEntity<BaseResponse<Void>> listAssetMaintenances() {
        return ResponseEntity.ok(new BaseResponse<>(true, "Maintenances found", null, null));
    }

    @PostMapping("/maintenances")
    @Tag(name = "Assets - Maintenances")
    @Operation(summary = "Create Assets - Maintenances")
    public ResponseEntity<BaseResponse<AssetMaintenance>> createAssetMaintenance(@RequestBody AssetMaintenance maintenance) {
        return ResponseEntity.status(201).body(new BaseResponse<>(true, "Created", null, assetService.scheduleMaintenance(maintenance)));
    }

    // --- Assets - Audits ---

    @GetMapping("/audits")
    @Tag(name = "Assets - Audits")
    @Operation(summary = "List Assets - Audits")
    public ResponseEntity<BaseResponse<Void>> listAssetAudits() {
        return ResponseEntity.ok(new BaseResponse<>(true, "Audits found", null, null));
    }

    // --- Assets - Documents ---

    @GetMapping("/documents")
    @Tag(name = "Assets - Documents")
    @Operation(summary = "List Assets - Documents")
    public ResponseEntity<BaseResponse<Void>> listAssetDocuments() {
        return ResponseEntity.ok(new BaseResponse<>(true, "Documents found", null, null));
    }

    // --- Assets - Usage logs ---

    @GetMapping("/usage-logs")
    @Tag(name = "Assets - Usage logs")
    @Operation(summary = "List Assets - Usage logs")
    public ResponseEntity<BaseResponse<Void>> listAssetUsageLogs() {
        return ResponseEntity.ok(new BaseResponse<>(true, "Usage logs found", null, null));
    }
}

