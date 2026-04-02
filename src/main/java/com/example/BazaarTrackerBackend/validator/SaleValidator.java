package com.example.BazaarTrackerBackend.validator;

import com.example.BazaarTrackerBackend.dto.sales.SaleRequest;
import com.example.BazaarTrackerBackend.model.enums.SaleType;

import org.springframework.stereotype.Component;

@Component
public class SaleValidator {

    public void validateCreate(SaleRequest request) {

        if (request == null) {
            throw new RuntimeException("Sale request cannot be null");
        }

        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new RuntimeException("Sale must contain at least one item");
        }

        // Validate each item
        request.getItems().forEach(item -> {
            if (item.getProductId() == null) {
                throw new RuntimeException("Product ID is required");
            }

            if (item.getQuantity() <= 0) {
                throw new RuntimeException("Quantity must be greater than 0");
            }
        });

        // CREDIT validation
        if (request.getSaleType() == SaleType.CREDIT &&
                request.getVendorId() == null) {
            throw new RuntimeException("Vendor required for credit sale");
        }
    }
}