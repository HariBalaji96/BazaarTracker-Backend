package com.example.BazaarTrackerBackend.mapper;

import com.example.BazaarTrackerBackend.dto.sales.SaleRequest;
import com.example.BazaarTrackerBackend.dto.sales.SaleResponse;
import com.example.BazaarTrackerBackend.model.entity.Sale;

import java.util.List;
import java.util.stream.Collectors;

public class SaleMapper {

    public static Sale toEntity(SaleRequest request) {

        Sale sale = new Sale();

        sale.setVendorId(request.getVendorId());
        sale.setSaleType(request.getSaleType());

        // Map items
        List<Sale.SaleItem> items = request.getItems()
                .stream()
                .map(item -> {
                    Sale.SaleItem saleItem = new Sale.SaleItem();
                    saleItem.setProductId(item.getProductId());
                    saleItem.setQuantity(item.getQuantity());
                    return saleItem;
                })
                .collect(Collectors.toList());

        sale.setItems(items);

        if (request.getSaleDate() != null && !request.getSaleDate().isEmpty()) {
            try {
                sale.setSaleDate(com.google.cloud.Timestamp.ofTimeMicroseconds(java.time.Instant.parse(request.getSaleDate()).toEpochMilli() * 1000));
            } catch (Exception e) {
                sale.setSaleDate(com.google.cloud.Timestamp.now());
            }
        } else {
            sale.setSaleDate(com.google.cloud.Timestamp.now());
        }

        return sale;
    }

    public static SaleResponse toResponse(Sale sale) {

        SaleResponse response = new SaleResponse();

        response.setId(sale.getId());
        response.setVendorId(sale.getVendorId());
        response.setUserId(sale.getUserId());
        response.setSaleType(sale.getSaleType());
        response.setTotalAmount(sale.getTotalAmount());
        
        if (sale.getSaleDate() != null) {
            response.setSaleDate(sale.getSaleDate().toDate().toString());
        }
        
        if (sale.getCreatedAt() != null) {
            response.setCreatedAt(sale.getCreatedAt().toDate().toString());
        }
        if (sale.getUpdatedAt() != null) {
            response.setUpdatedAt(sale.getUpdatedAt().toDate().toString());
        }

        if (sale.getItems() != null) {
            List<SaleResponse.SaleItemResponse> responseItems = sale.getItems().stream().map(item -> {
                SaleResponse.SaleItemResponse itemResponse = new SaleResponse.SaleItemResponse();
                itemResponse.setProductId(item.getProductId());
                itemResponse.setQuantity(item.getQuantity());
                itemResponse.setPrice(item.getPrice());
                return itemResponse;
            }).collect(Collectors.toList());
            response.setItems(responseItems);
        }

        return response;
    }
}