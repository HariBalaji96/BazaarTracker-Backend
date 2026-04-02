package com.example.BazaarTrackerBackend.model.entity;

import lombok.Data;

@Data
public class SaleItem {
    private String productId;
    private String productName;
    private int quantity;
    private double price;
    private double total;
}