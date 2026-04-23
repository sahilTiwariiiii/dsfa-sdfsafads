package com.example.samrat.modules.inventory.service;

import com.example.samrat.core.context.TenantContext;
import com.example.samrat.modules.inventory.pharmacy.entity.PharmacyStock;
import com.example.samrat.modules.inventory.pharmacy.repository.PharmacyStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.example.samrat.core.context.TenantContext;
import com.example.samrat.modules.inventory.pharmacy.entity.PharmacyStock;
import com.example.samrat.modules.inventory.pharmacy.repository.PharmacyStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final PharmacyStockRepository pharmacyStockRepository;

    @Transactional
    public PharmacyStock addStock(PharmacyStock stock) {
        stock.setHospitalId(TenantContext.getHospitalId());
        stock.setBranchId(TenantContext.getBranchId());
        return pharmacyStockRepository.save(stock);
    }

    public Page<PharmacyStock> getAllStock(Pageable pageable) {
        return pharmacyStockRepository.findByHospitalIdAndBranchId(
                TenantContext.getHospitalId(), TenantContext.getBranchId(), pageable);
    }

    public Page<PharmacyStock> searchStock(String name, String genericName, String batchNumber, Pageable pageable) {
        return pharmacyStockRepository.searchStock(
                TenantContext.getHospitalId(), TenantContext.getBranchId(),
                name, genericName, batchNumber, pageable);
    }

    @Transactional
    public PharmacyStock updateStock(Long id, PharmacyStock stockDetails) {
        PharmacyStock stock = pharmacyStockRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stock item not found"));
        
        stock.setMedicineName(stockDetails.getMedicineName());
        stock.setGenericName(stockDetails.getGenericName());
        stock.setBatchNumber(stockDetails.getBatchNumber());
        stock.setQuantity(stockDetails.getQuantity());
        stock.setUnitPrice(stockDetails.getUnitPrice());
        stock.setMrp(stockDetails.getMrp());
        stock.setExpiryDate(stockDetails.getExpiryDate());
        stock.setManufacturer(stockDetails.getManufacturer());
        stock.setReorderLevel(stockDetails.getReorderLevel());
        
        return pharmacyStockRepository.save(stock);
    }

    @Transactional
    public void deleteStock(Long id) {
        pharmacyStockRepository.deleteById(id);
    }
}
