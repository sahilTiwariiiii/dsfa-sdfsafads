package com.example.samrat.modules.inventory.controller;

import com.example.samrat.core.dto.BaseResponse;
import com.example.samrat.modules.inventory.pharmacy.entity.PharmacyStock;
import com.example.samrat.modules.inventory.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
@Tag(name = "Inventory Management", description = "APIs for pharmacy stock and hospital asset management")
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping("/pharmacy/stock")
    @PreAuthorize("hasAuthority('INVENTORY_WRITE')")
    @Operation(summary = "Add pharmacy stock", description = "Registers new stock items in the hospital pharmacy")
    public ResponseEntity<BaseResponse<PharmacyStock>> addStock(
            @RequestParam String itemName,
            @RequestParam String batchNumber,
            @RequestParam Integer quantity,
            @RequestParam Double unitPrice) {
        PharmacyStock stock = inventoryService.addStock(itemName, batchNumber, quantity, unitPrice);
        return ResponseEntity.ok(new BaseResponse<>(true, "Stock added successfully", null, stock));
    }

    @GetMapping("/pharmacy/stock")
    @PreAuthorize("hasAuthority('INVENTORY_READ')")
    @Operation(summary = "Get all stock", description = "Retrieves all items currently in the pharmacy inventory")
    public ResponseEntity<BaseResponse<List<PharmacyStock>>> getAllStock() {
        List<PharmacyStock> stock = inventoryService.getAllStock();
        return ResponseEntity.ok(new BaseResponse<>(true, "Stock found", null, stock));
    }
}
