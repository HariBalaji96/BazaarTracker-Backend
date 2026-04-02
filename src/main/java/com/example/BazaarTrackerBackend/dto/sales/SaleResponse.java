package com.example.BazaarTrackerBackend.dto.sales;

import com.example.BazaarTrackerBackend.model.enums.SaleType;

public class SaleResponse {

    private String id;
    private String vendorId;
    private SaleType saleType;
    private double totalAmount;

    public SaleResponse() {}

    // getters & setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public SaleType getSaleType() {
        return saleType;
    }

    public void setSaleType(SaleType saleType) {
        this.saleType = saleType;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}