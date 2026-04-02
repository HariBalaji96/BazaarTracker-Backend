package com.example.BazaarTrackerBackend.dto.sales;

import com.example.BazaarTrackerBackend.model.enums.SaleType;

import java.util.List;

public class SaleRequest {

    private String vendorId;
    private SaleType saleType;
    private List<SaleItemRequest> items;

    public SaleRequest() {}

    // getters & setters

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

    public List<SaleItemRequest> getItems() {
        return items;
    }

    public void setItems(List<SaleItemRequest> items) {
        this.items = items;
    }

    // 🔹 Inner class for items
    public static class SaleItemRequest {
        private String productId;
        private int quantity;

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }
}