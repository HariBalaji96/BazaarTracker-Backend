package com.example.BazaarTrackerBackend.validator;

import com.example.BazaarTrackerBackend.dto.payment.PaymentRequest;

import org.springframework.stereotype.Component;

@Component
public class PaymentValidator {

    public void validateCreate(PaymentRequest request) {

        if (request == null) {
            throw new RuntimeException("Payment request cannot be null");
        }

        if (request.getVendorId() == null || request.getVendorId().isBlank()) {
            throw new RuntimeException("Vendor ID is required");
        }

        if (request.getAmount() <= 0) {
            throw new RuntimeException("Payment amount must be greater than 0");
        }
    }
}