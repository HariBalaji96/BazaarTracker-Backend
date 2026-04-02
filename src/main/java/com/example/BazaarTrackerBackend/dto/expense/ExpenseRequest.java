package com.example.BazaarTrackerBackend.dto.expense;

public class ExpenseRequest {

    private String category;
    private double amount;

    public ExpenseRequest() {}

    // getters & setters

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}