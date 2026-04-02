package com.example.BazaarTrackerBackend.mapper;

import com.example.BazaarTrackerBackend.dto.product.ProductRequest;
import com.example.BazaarTrackerBackend.dto.product.ProductResponse;
import com.example.BazaarTrackerBackend.model.entity.Product;

public class ProductMapper {

    public static Product toEntity(ProductRequest request) {

        Product product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());

        return product;
    }

    public static ProductResponse toResponse(Product product) {

        ProductResponse response = new ProductResponse();

        response.setId(product.getId());
        response.setName(product.getName());
        response.setPrice(product.getPrice());
        response.setStock(product.getStock());
        response.setActive(product.isActive());

        return response;
    }
}