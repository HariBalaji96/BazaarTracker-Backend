package com.example.BazaarTrackerBackend.controller.payment;

import com.example.BazaarTrackerBackend.dto.payment.*;
import com.example.BazaarTrackerBackend.service.payment.PaymentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> create(@RequestBody PaymentRequest request) throws Exception {
        return ResponseEntity.ok(paymentService.createPayment(request));
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponse>> getAll() throws Exception {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }
}