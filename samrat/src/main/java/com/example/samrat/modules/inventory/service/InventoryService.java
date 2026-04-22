package com.example.samrat.modules.inventory.service;

import com.example.samrat.core.context.TenantContext;
import com.example.samrat.modules.inventory.pharmacy.entity.PharmacyStock;
import com.example.samrat.modules.inventory.pharmacy.repository.PharmacyStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final PharmacyStockRepository pharmacyStockRepository;

    @Transactional
    public PharmacyStock addStock(String itemName, String batchNumber, Integer quantity, Double unitPrice) {
        PharmacyStock stock = new PharmacyStock();
        stock.setMedicineName(itemName);
        stock.setBatchNumber(batchNumber);
        stock.setQuantity(quantity);
        stock.setUnitPrice(unitPrice);
        stock.setGenericName("Generic " + itemName); // Dummy data
        stock.setManufacturer("PharmaCorp"); // Dummy data
        stock.setExpiryDate(java.time.LocalDate.now().plusYears(2)); // Dummy data
        stock.setMrp(unitPrice * 1.2); // Dummy data
        stock.setReorderLevel(10); // Dummy data
        stock.setHospitalId(TenantContext.getHospitalId());
        stock.setBranchId(TenantContext.getBranchId());

        return pharmacyStockRepository.save(stock);
    }

    public List<PharmacyStock> getAllStock() {
        return pharmacyStockRepository.findByHospitalIdAndBranchId(
                TenantContext.getHospitalId(), TenantContext.getBranchId());
    }
}
