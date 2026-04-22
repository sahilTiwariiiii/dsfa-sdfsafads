package com.example.samrat.modules.integration.service.impl;

import com.example.samrat.modules.integration.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MockSmsService implements SmsService {
    @Override
    public void sendSms(String phoneNumber, String message) {
        log.info("Sending SMS to {}: {}", phoneNumber, message);
        // In production, integrate with Twilio, AWS SNS, etc.
    }
}
