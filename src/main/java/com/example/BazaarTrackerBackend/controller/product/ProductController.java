package com.example.BazaarTrackerBackend.controller.product;

import com.example.BazaarTrackerBackend.dto.product.*;
import com.example.BazaarTrackerBackend.service.product.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> create(@RequestBody ProductRequest request) throws Exception {
        return ResponseEntity.ok(productService.createProduct(request));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAll() throws Exception {
        return ResponseEntity.ok(productService.getAllProducts());
    }
}