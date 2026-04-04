package com.example.BazaarTrackerBackend.service.vendor;

import com.example.BazaarTrackerBackend.dto.vendor.VendorRequest;
import com.example.BazaarTrackerBackend.dto.vendor.VendorResponse;
import com.example.BazaarTrackerBackend.exception.CustomException;
import com.example.BazaarTrackerBackend.mapper.VendorMapper;
import com.example.BazaarTrackerBackend.model.entity.Vendor;
import com.example.BazaarTrackerBackend.repository.firestore.FirestoreRepository;
import com.example.BazaarTrackerBackend.constants.CollectionNames;
import com.example.BazaarTrackerBackend.validator.VendorValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import com.google.cloud.Timestamp;
import java.util.List;

@Service
public class VendorService {

    @Autowired
    private FirestoreRepository firestoreRepository;

    @Autowired
    private VendorValidator vendorValidator;

    private static final String COLLECTION = CollectionNames.VENDORS;

    private String getCurrentUserId() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public VendorResponse createVendor(VendorRequest request) throws Exception {
        vendorValidator.validateCreate(request);
        Vendor vendor = VendorMapper.toEntity(request);

        vendor.setUserId(getCurrentUserId());
        vendor.setTotalCreditGiven(0.0);
        vendor.setTotalPaidAmount(0.0);
        vendor.setPendingAmount(0.0);
        vendor.setActive(true);
        vendor.setCreatedAt(Timestamp.now());
        vendor.setUpdatedAt(Timestamp.now());

        Vendor saved = firestoreRepository.save(COLLECTION, vendor);
        return VendorMapper.toResponse(saved);
    }

    public List<VendorResponse> getAllVendors() throws Exception {
        return firestoreRepository.findByField(COLLECTION, "userId", getCurrentUserId(), Vendor.class)
                .stream()
                .map(VendorMapper::toResponse)
                .toList();
    }

    public VendorResponse getVendorById(String id) throws Exception {
        Vendor vendor = getVendorEntity(id);
        return VendorMapper.toResponse(vendor);
    }

    public VendorResponse updateVendor(String id, VendorRequest request) throws Exception {
        Vendor vendor = getVendorEntity(id);

        if (request.getName() != null && !request.getName().trim().isEmpty()) {
            vendor.setName(request.getName());
        }
        if (request.getPhone() != null && !request.getPhone().trim().isEmpty()) {
            vendor.setPhone(request.getPhone());
        }
        if (request.getAddress() != null && !request.getAddress().trim().isEmpty()) {
            vendor.setAddress(request.getAddress());
        }

        saveVendor(vendor);
        return VendorMapper.toResponse(vendor);
    }

    public void deleteVendor(String id) throws Exception {
        getVendorEntity(id);
        firestoreRepository.delete(COLLECTION, id);
    }

    public void addCredit(String vendorId, double amount) throws Exception {
        Vendor vendor = getVendorEntity(vendorId);
        vendor.setTotalCreditGiven(vendor.getTotalCreditGiven() + amount);
        updatePending(vendor);
        saveVendor(vendor);
    }

    public void addPayment(String vendorId, double amount) throws Exception {
        Vendor vendor = getVendorEntity(vendorId);
        vendor.setTotalPaidAmount(vendor.getTotalPaidAmount() + amount);
        updatePending(vendor);
        saveVendor(vendor);
    }

    private Vendor getVendorEntity(String vendorId) throws Exception {
        Vendor vendor = firestoreRepository.findById(COLLECTION, vendorId, Vendor.class);
        if (vendor == null) {
            throw new CustomException("Vendor not found", 404);
        }
        if (vendor.getUserId() == null || !vendor.getUserId().equals(getCurrentUserId())) {
            throw new CustomException("Unauthorized access", 403);
        }
        return vendor;
    }

    private void saveVendor(Vendor vendor) throws Exception {
        vendor.setUpdatedAt(Timestamp.now());
        firestoreRepository.save(COLLECTION, vendor);
    }

    private void updatePending(Vendor vendor) {
        vendor.setPendingAmount(vendor.getTotalCreditGiven() - vendor.getTotalPaidAmount());
    }
}
