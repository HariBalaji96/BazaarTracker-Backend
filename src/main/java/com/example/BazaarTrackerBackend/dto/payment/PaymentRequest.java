package com.example.BazaarTrackerBackend.dto.payment;

public class PaymentRequest {

    private String vendorId;
    private double amount;

    public PaymentRequest() {}

    // getters & setters

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}