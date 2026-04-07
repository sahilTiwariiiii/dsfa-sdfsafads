package com.example.samrat.modules.inventory.purchase.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "purchase_order_items")
@Getter
@Setter
public class PurchaseOrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_order_id", nullable = false)
    private PurchaseOrder purchaseOrder;

    @Column(nullable = false)
    private String itemName;

    @Column(nullable = false)
    private Double unitPrice;

    @Column(nullable = false)
    private Double quantity;

    @Column(nullable = false)
    private Double totalAmount;

    @Column(name = "item_id")
    private Long itemId; // Reference to StoreItem or PharmacyStock
}
