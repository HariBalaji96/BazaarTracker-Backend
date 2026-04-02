package com.example.BazaarTrackerBackend.validator;

import com.example.BazaarTrackerBackend.dto.vendor.VendorRequest;
import com.example.BazaarTrackerBackend.constants.AppConstants;

import org.springframework.stereotype.Component;

@Component
public class VendorValidator {

    public void validateCreate(VendorRequest request) {

        if (request == null) {
            throw new RuntimeException("Vendor request cannot be null");
        }

        if (request.getName() == null || request.getName().isBlank()) {
            throw new RuntimeException("Vendor name is required");
        }

        if (request.getPhone() == null || request.getPhone().isBlank()) {
            throw new RuntimeException("Vendor phone is required");
        }

        if (request.getAddress() == null || request.getAddress().isBlank()) {
            throw new RuntimeException("Vendor address is required");
        }
    }
}