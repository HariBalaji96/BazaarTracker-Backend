package com.example.BazaarTrackerBackend.mapper;

import com.example.BazaarTrackerBackend.dto.payment.PaymentRequest;
import com.example.BazaarTrackerBackend.dto.payment.PaymentResponse;
import com.example.BazaarTrackerBackend.model.entity.Payment;

public class PaymentMapper {

    public static Payment toEntity(PaymentRequest request) {

        Payment payment = new Payment();
        payment.setVendorId(request.getVendorId());
        payment.setAmount(request.getAmount());

        return payment;
    }

    public static PaymentResponse toResponse(Payment payment) {

        PaymentResponse response = new PaymentResponse();

        response.setId(payment.getId());
        response.setVendorId(payment.getVendorId());
        response.setAmount(payment.getAmount());

        return response;
    }
}