package com.example.samrat.modules.inventory.controller;

import com.example.samrat.core.dto.BaseResponse;
import com.example.samrat.modules.inventory.pharmacy.entity.PharmacyStock;
import com.example.samrat.modules.inventory.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
@Tag(name = "Inventory Management", description = "APIs for pharmacy stock and hospital asset management")
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping("/pharmacy/stock")
    @PreAuthorize("hasAuthority('INVENTORY_WRITE')")
    @Operation(summary = "Add pharmacy stock", description = "Registers new stock items in the hospital pharmacy")
    public ResponseEntity<BaseResponse<PharmacyStock>> addStock(@RequestBody PharmacyStock stock) {
        PharmacyStock created = inventoryService.addStock(stock);
        return ResponseEntity.ok(new BaseResponse<>(true, "Stock added successfully", null, created));
    }

    @GetMapping("/pharmacy/stock")
    @PreAuthorize("hasAuthority('INVENTORY_READ')")
    @Operation(summary = "Method Summary", description = "Retrieves all items currently in the pharmacy inventory")
    public ResponseEntity<BaseResponse<Page<PharmacyStock>>> getAllStock(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PharmacyStock> stock = inventoryService.getAllStock(pageable);
        return ResponseEntity.ok(new BaseResponse<>(true, "Stock found", null, stock));
    }

    @GetMapping("/pharmacy/stock/search")
    @PreAuthorize("hasAuthority('INVENTORY_READ')")
    @Operation(summary = "Search pharmacy stock", description = "Filters pharmacy stock by name, generic name, and batch number")
    public ResponseEntity<BaseResponse<Page<PharmacyStock>>> searchStock(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String genericName,
            @RequestParam(required = false) String batchNumber,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PharmacyStock> stock = inventoryService.searchStock(name, genericName, batchNumber, pageable);
        return ResponseEntity.ok(new BaseResponse<>(true, "Search results found", null, stock));
    }

    @PutMapping("/pharmacy/stock/{id}")
    @PreAuthorize("hasAuthority('INVENTORY_WRITE')")
    @Operation(summary = "Update pharmacy stock", description = "Updates an existing stock item's details")
    public ResponseEntity<BaseResponse<PharmacyStock>> updateStock(
            @PathVariable Long id,
            @RequestBody PharmacyStock stock) {
        PharmacyStock updated = inventoryService.updateStock(id, stock);
        return ResponseEntity.ok(new BaseResponse<>(true, "Stock updated successfully", null, updated));
    }

    @DeleteMapping("/pharmacy/stock/{id}")
    @PreAuthorize("hasAuthority('INVENTORY_DELETE')")
    @Operation(summary = "Method Summary", description = "Removes a stock item from the pharmacy inventory")
    public ResponseEntity<BaseResponse<Void>> deleteStock(@PathVariable Long id) {
        inventoryService.deleteStock(id);
        return ResponseEntity.ok(new BaseResponse<>(true, "Stock deleted successfully", null, null));
    }
}

