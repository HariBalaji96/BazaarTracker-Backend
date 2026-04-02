package com.example.BazaarTrackerBackend.service.payment;

import com.example.BazaarTrackerBackend.dto.payment.PaymentRequest;
import com.example.BazaarTrackerBackend.dto.payment.PaymentResponse;

import com.example.BazaarTrackerBackend.mapper.PaymentMapper;

import com.example.BazaarTrackerBackend.model.entity.Payment;
import com.example.BazaarTrackerBackend.model.entity.Vendor;

import com.example.BazaarTrackerBackend.repository.firestore.FirestoreRepository;

import com.example.BazaarTrackerBackend.constants.CollectionNames;

import com.example.BazaarTrackerBackend.service.vendor.VendorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private FirestoreRepository firestoreRepository;

    @Autowired
    private VendorService vendorService;

    private static final String COLLECTION = CollectionNames.PAYMENTS;

    // ✅ CREATE PAYMENT
    public PaymentResponse createPayment(PaymentRequest request) throws Exception {

        // ⚠️ Step 1: Validate input
        if (request.getAmount() <= 0) {
            throw new RuntimeException("Payment amount must be greater than 0");
        }

        if (request.getVendorId() == null) {
            throw new RuntimeException("Vendor is required");
        }

        // ⚠️ Step 2: Fetch vendor
        Vendor vendor = firestoreRepository.findById(
                CollectionNames.VENDORS,
                request.getVendorId(),
                Vendor.class
        );

        if (vendor == null) {
            throw new RuntimeException("Vendor not found");
        }

        // ⚠️ Step 3: Prevent overpayment
        if (request.getAmount() > vendor.getPendingAmount()) {
            throw new RuntimeException("Payment exceeds pending amount");
        }

        // ⚠️ Step 4: Update vendor credit system
        vendorService.addPayment(request.getVendorId(), request.getAmount());

        // ⚠️ Step 5: Save payment record
        Payment payment = PaymentMapper.toEntity(request);

        payment.setCreatedAt(LocalDateTime.now());
        payment.setUpdatedAt(LocalDateTime.now());

        Payment saved = firestoreRepository.save(COLLECTION, payment);

        return PaymentMapper.toResponse(saved);
    }

    // ✅ GET ALL PAYMENTS
    public List<PaymentResponse> getAllPayments() throws Exception {

        return firestoreRepository.findAll(COLLECTION, Payment.class)
                .stream()
                .map(PaymentMapper::toResponse)
                .toList();
    }

    // ✅ GET PAYMENT BY ID
    public PaymentResponse getPaymentById(String id) throws Exception {

        Payment payment = firestoreRepository.findById(COLLECTION, id, Payment.class);

        if (payment == null) {
            throw new RuntimeException("Payment not found");
        }

        return PaymentMapper.toResponse(payment);
    }
}