package com.example.BazaarTrackerBackend.controller.sales;

import com.example.BazaarTrackerBackend.dto.sales.*;
import com.example.BazaarTrackerBackend.service.sales.SalesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales")
public class SalesController {

    @Autowired
    private SalesService salesService;

    @PostMapping
    public ResponseEntity<SaleResponse> create(@RequestBody SaleRequest request) throws Exception {
        return ResponseEntity.ok(salesService.createSale(request));
    }

    @GetMapping
    public ResponseEntity<List<SaleResponse>> getAll() throws Exception {
        return ResponseEntity.ok(salesService.getAllSales());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleResponse> getById(@PathVariable String id) throws Exception {
        return ResponseEntity.ok(salesService.getSaleById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SaleResponse> update(@PathVariable String id, @RequestBody SaleRequest request) throws Exception {
        return ResponseEntity.ok(salesService.updateSale(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) throws Exception {
        salesService.deleteSale(id);
        return ResponseEntity.noContent().build();
    }
}