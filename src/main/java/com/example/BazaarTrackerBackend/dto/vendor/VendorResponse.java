package com.example.BazaarTrackerBackend.dto.vendor;

public class VendorResponse {

    private String id;
    private String name;
    private String phone;
    private String address;

    private double totalCreditGiven;
    private double totalPaidAmount;
    private double pendingAmount;

    private boolean isActive;

    public VendorResponse() {}

    // getters & setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getTotalCreditGiven() {
        return totalCreditGiven;
    }

    public void setTotalCreditGiven(double totalCreditGiven) {
        this.totalCreditGiven = totalCreditGiven;
    }

    public double getTotalPaidAmount() {
        return totalPaidAmount;
    }

    public void setTotalPaidAmount(double totalPaidAmount) {
        this.totalPaidAmount = totalPaidAmount;
    }

    public double getPendingAmount() {
        return pendingAmount;
    }

    public void setPendingAmount(double pendingAmount) {
        this.pendingAmount = pendingAmount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}