package com.example.samrat.modules.integration.service;

import java.util.Map;

public interface PaymentService {
    String createPaymentOrder(Double amount, String currency, String receiptId);
    boolean verifyPayment(String paymentId, String orderId, String signature);
}
