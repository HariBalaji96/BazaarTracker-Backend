package com.example.BazaarTrackerBackend.mapper;

import com.example.BazaarTrackerBackend.dto.payment.PaymentRequest;
import com.example.BazaarTrackerBackend.dto.payment.PaymentResponse;
import com.example.BazaarTrackerBackend.model.entity.Payment;

public class PaymentMapper {

    public static Payment toEntity(PaymentRequest request) {

        Payment payment = new Payment();
        payment.setVendorId(request.getVendorId());
        payment.setAmount(request.getAmount());
        payment.setPaymentMethod(request.getPaymentMethod());

        if (request.getDate() != null && !request.getDate().isEmpty()) {
            try {
                payment.setDate(com.google.cloud.Timestamp.ofTimeMicroseconds(java.time.Instant.parse(request.getDate()).toEpochMilli() * 1000));
            } catch (Exception e) {
                payment.setDate(com.google.cloud.Timestamp.now());
            }
        } else {
            payment.setDate(com.google.cloud.Timestamp.now());
        }

        return payment;
    }

    public static PaymentResponse toResponse(Payment payment) {

        PaymentResponse response = new PaymentResponse();

        response.setId(payment.getId());
        response.setUserId(payment.getUserId());
        response.setVendorId(payment.getVendorId());
        response.setAmount(payment.getAmount());
        response.setPaymentMethod(payment.getPaymentMethod());

        if (payment.getDate() != null) {
            response.setDate(payment.getDate().toDate().toString());
        }
        if (payment.getCreatedAt() != null) {
            response.setCreatedAt(payment.getCreatedAt().toDate().toString());
        }
        if (payment.getUpdatedAt() != null) {
            response.setUpdatedAt(payment.getUpdatedAt().toDate().toString());
        }

        return response;
    }
}