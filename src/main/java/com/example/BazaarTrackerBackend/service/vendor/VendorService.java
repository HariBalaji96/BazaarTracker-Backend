package com.example.BazaarTrackerBackend.service.vendor;

import com.example.BazaarTrackerBackend.dto.vendor.VendorRequest;
import com.example.BazaarTrackerBackend.dto.vendor.VendorResponse;

import com.example.BazaarTrackerBackend.mapper.VendorMapper;

import com.example.BazaarTrackerBackend.model.entity.Vendor;

import com.example.BazaarTrackerBackend.repository.firestore.FirestoreRepository;

import com.example.BazaarTrackerBackend.constants.CollectionNames;

import com.example.BazaarTrackerBackend.validator.VendorValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VendorService {

    @Autowired
    private FirestoreRepository firestoreRepository;

    @Autowired
    private VendorValidator vendorValidator;

    private static final String COLLECTION = CollectionNames.VENDORS;

    // ✅ CREATE VENDOR
    public VendorResponse createVendor(VendorRequest request) throws Exception {

        vendorValidator.validateCreate(request);

        Vendor vendor = VendorMapper.toEntity(request);

        vendor.setTotalCreditGiven(0.0);
        vendor.setTotalPaidAmount(0.0);
        vendor.setPendingAmount(0.0);
        vendor.setActive(true);

        vendor.setCreatedAt(LocalDateTime.now());
        vendor.setUpdatedAt(LocalDateTime.now());

        Vendor saved = firestoreRepository.save(COLLECTION, vendor);

        return VendorMapper.toResponse(saved);
    }

    // ✅ GET ALL
    public List<VendorResponse> getAllVendors() throws Exception {
        return firestoreRepository.findAll(COLLECTION, Vendor.class)
                .stream()
                .map(VendorMapper::toResponse)
                .toList();
    }

    // ✅ GET BY ID
    public VendorResponse getVendorById(String id) throws Exception {

        Vendor vendor = firestoreRepository.findById(COLLECTION, id, Vendor.class);

        if (vendor == null) {
            throw new RuntimeException("Vendor not found");
        }

        return VendorMapper.toResponse(vendor);
    }

    // 🔥 ADD CREDIT (Called from SalesService)
    public void addCredit(String vendorId, double amount) throws Exception {

        Vendor vendor = getVendorEntity(vendorId);

        // Increase credit
        vendor.setTotalCreditGiven(vendor.getTotalCreditGiven() + amount);

        updatePending(vendor);

        saveVendor(vendor);
    }

    // 🔥 ADD PAYMENT (Called from PaymentService)
    public void addPayment(String vendorId, double amount) throws Exception {

        Vendor vendor = getVendorEntity(vendorId);

        // Increase paid amount
        vendor.setTotalPaidAmount(vendor.getTotalPaidAmount() + amount);

        updatePending(vendor);

        saveVendor(vendor);
    }

    // 🔒 INTERNAL: Fetch entity
    private Vendor getVendorEntity(String vendorId) throws Exception {
        Vendor vendor = firestoreRepository.findById(COLLECTION, vendorId, Vendor.class);

        if (vendor == null) {
            throw new RuntimeException("Vendor not found");
        }

        return vendor;
    }

    // 🔒 INTERNAL: Save
    private void saveVendor(Vendor vendor) throws Exception {
        vendor.setUpdatedAt(LocalDateTime.now());
        firestoreRepository.save(COLLECTION, vendor);
    }

    // 🔒 INTERNAL: Pending calculation
    private void updatePending(Vendor vendor) {
        vendor.setPendingAmount(
                vendor.getTotalCreditGiven() - vendor.getTotalPaidAmount()
        );
    }
}