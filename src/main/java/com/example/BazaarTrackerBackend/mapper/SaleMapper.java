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

        return sale;
    }

    public static SaleResponse toResponse(Sale sale) {

        SaleResponse response = new SaleResponse();

        response.setId(sale.getId());
        response.setVendorId(sale.getVendorId());
        response.setSaleType(sale.getSaleType());
        response.setTotalAmount(sale.getTotalAmount());

        return response;
    }
}