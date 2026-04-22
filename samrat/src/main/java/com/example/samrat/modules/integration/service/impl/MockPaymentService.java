package com.example.samrat.modules.integration.service.impl;

import com.example.samrat.modules.integration.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class MockPaymentService implements PaymentService {

    @Override
    public String createPaymentOrder(Double amount, String currency, String receiptId) {
        log.info("Creating payment order for amount: {} {}", amount, currency);
        return "order_" + UUID.randomUUID().toString();
    }

    @Override
    public boolean verifyPayment(String paymentId, String orderId, String signature) {
        log.info("Verifying payment: {} for order: {}", paymentId, orderId);
        return true; // Mock verification always returns true
    }
}
