package com.example.BazaarTrackerBackend.controller.vendor;

import com.example.BazaarTrackerBackend.dto.vendor.*;
import com.example.BazaarTrackerBackend.service.vendor.VendorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendors")
public class VendorController {

    @Autowired
    private VendorService vendorService;

    @PostMapping
    public ResponseEntity<VendorResponse> create(@RequestBody VendorRequest request) throws Exception {
        return ResponseEntity.ok(vendorService.createVendor(request));
    }

    @GetMapping
    public ResponseEntity<List<VendorResponse>> getAll() throws Exception {
        return ResponseEntity.ok(vendorService.getAllVendors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VendorResponse> getById(@PathVariable String id) throws Exception {
        return ResponseEntity.ok(vendorService.getVendorById(id));
    }
}