package com.example.BazaarTrackerBackend.service.payment;

import com.example.BazaarTrackerBackend.dto.payment.PaymentRequest;
import com.example.BazaarTrackerBackend.dto.payment.PaymentResponse;
import com.example.BazaarTrackerBackend.exception.CustomException;
import com.example.BazaarTrackerBackend.mapper.PaymentMapper;
import com.example.BazaarTrackerBackend.model.entity.Payment;
import com.example.BazaarTrackerBackend.model.entity.Vendor;
import com.example.BazaarTrackerBackend.repository.firestore.FirestoreRepository;
import com.example.BazaarTrackerBackend.constants.CollectionNames;
import com.example.BazaarTrackerBackend.service.vendor.VendorService;
import com.example.BazaarTrackerBackend.service.dashboard.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import com.google.cloud.Timestamp;
import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private FirestoreRepository firestoreRepository;

    @Autowired
    private VendorService vendorService;

    @Autowired
    private DashboardService dashboardService;

    private static final String COLLECTION = CollectionNames.PAYMENTS;

    private String getCurrentUserId() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public PaymentResponse createPayment(PaymentRequest request) throws Exception {

        if (request.getAmount() <= 0) {
            throw new CustomException("Payment amount must be greater than 0", 400);
        }

        if (request.getVendorId() == null) {
            throw new CustomException("Vendor is required", 400);
        }

        // Fetch Vendor and assert authority natively
        Vendor vendor = firestoreRepository.findById(CollectionNames.VENDORS, request.getVendorId(), Vendor.class);

        if (vendor == null || !vendor.getUserId().equals(getCurrentUserId())) {
            throw new CustomException("Vendor not found or unauthorized", 404);
        }

        if (request.getAmount() > vendor.getPendingAmount()) {
            throw new CustomException("Payment exceeds pending amount", 400);
        }

        // Add payment update internally handled in VendorService
        vendorService.addPayment(request.getVendorId(), request.getAmount());

        Payment payment = PaymentMapper.toEntity(request);
        String userId = getCurrentUserId();
        payment.setUserId(userId);
        payment.setCreatedAt(Timestamp.now());
        payment.setUpdatedAt(Timestamp.now());

        Payment saved = firestoreRepository.save(COLLECTION, payment);

        // Update Dashboard
        dashboardService.recordPayment(userId, request.getAmount());

        return PaymentMapper.toResponse(saved);
    }

    public List<PaymentResponse> getAllPayments() throws Exception {
        return firestoreRepository.findByField(COLLECTION, "userId", getCurrentUserId(), Payment.class)
                .stream()
                .map(PaymentMapper::toResponse)
                .toList();
    }

    public PaymentResponse getPaymentById(String id) throws Exception {
        Payment payment = firestoreRepository.findById(COLLECTION, id, Payment.class);

        if (payment == null) {
            throw new CustomException("Payment not found", 404);
        }

        if (payment.getUserId() == null || !payment.getUserId().equals(getCurrentUserId())) {
            throw new CustomException("Unauthorized access", 403);
        }

        return PaymentMapper.toResponse(payment);
    }
}
