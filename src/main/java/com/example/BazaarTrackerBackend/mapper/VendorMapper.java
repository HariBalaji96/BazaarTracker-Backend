package com.example.BazaarTrackerBackend.mapper;

import com.example.BazaarTrackerBackend.dto.vendor.VendorRequest;
import com.example.BazaarTrackerBackend.dto.vendor.VendorResponse;
import com.example.BazaarTrackerBackend.model.entity.Vendor;

public class VendorMapper {

    public static Vendor toEntity(VendorRequest request) {

        Vendor vendor = new Vendor();
        vendor.setName(request.getName());
        vendor.setPhone(request.getPhone());
        vendor.setAddress(request.getAddress());

        return vendor;
    }

    public static VendorResponse toResponse(Vendor vendor) {

        VendorResponse response = new VendorResponse();

        response.setId(vendor.getId());
        response.setName(vendor.getName());
        response.setPhone(vendor.getPhone());
        response.setAddress(vendor.getAddress());

        response.setTotalCreditGiven(vendor.getTotalCreditGiven());
        response.setTotalPaidAmount(vendor.getTotalPaidAmount());
        response.setPendingAmount(vendor.getPendingAmount());

        response.setActive(vendor.isActive());

        return response;
    }
}