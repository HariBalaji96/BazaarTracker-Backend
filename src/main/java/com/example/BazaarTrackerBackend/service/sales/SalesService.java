package com.example.BazaarTrackerBackend.service.sales;

import com.example.BazaarTrackerBackend.dto.sales.SaleRequest;
import com.example.BazaarTrackerBackend.dto.sales.SaleResponse;

import com.example.BazaarTrackerBackend.mapper.SaleMapper;

import com.example.BazaarTrackerBackend.model.entity.Product;
import com.example.BazaarTrackerBackend.model.entity.Sale;
import com.example.BazaarTrackerBackend.model.enums.SaleType;

import com.example.BazaarTrackerBackend.repository.firestore.FirestoreRepository;

import com.example.BazaarTrackerBackend.constants.CollectionNames;

import com.example.BazaarTrackerBackend.service.product.ProductService;
import com.example.BazaarTrackerBackend.service.vendor.VendorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SalesService {

    @Autowired
    private FirestoreRepository firestoreRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private VendorService vendorService;

    private static final String SALE_COLLECTION = CollectionNames.SALES;

    // ✅ CREATE SALE
    public SaleResponse createSale(SaleRequest request) throws Exception {

        // ⚠️ Step 1: Validate
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new RuntimeException("Sale must contain at least one item");
        }

        double totalAmount = 0;

        // ⚠️ Step 2: Calculate total + validate stock
        for (var item : request.getItems()) {

            Product product = firestoreRepository.findById(
                    CollectionNames.PRODUCTS,
                    item.getProductId(),
                    Product.class
            );

            if (product == null) {
                throw new RuntimeException("Product not found: " + item.getProductId());
            }

            if (product.getStock() < item.getQuantity()) {
                throw new RuntimeException("Insufficient stock for: " + product.getName());
            }

            double itemTotal = product.getPrice() * item.getQuantity();
            totalAmount += itemTotal;
        }

        // ⚠️ Step 3: Reduce stock (AFTER validation)
        for (var item : request.getItems()) {
            productService.reduceStock(item.getProductId(), item.getQuantity());
        }

        // ⚠️ Step 4: CREDIT handling
        if (request.getSaleType() == SaleType.CREDIT) {

            if (request.getVendorId() == null) {
                throw new RuntimeException("Vendor required for credit sale");
            }

            vendorService.addCredit(request.getVendorId(), totalAmount);
        }

        // ⚠️ Step 5: Save sale
        Sale sale = SaleMapper.toEntity(request);

        sale.setTotalAmount(totalAmount);
        sale.setCreatedAt(LocalDateTime.now());
        sale.setUpdatedAt(LocalDateTime.now());

        Sale saved = firestoreRepository.save(SALE_COLLECTION, sale);

        return SaleMapper.toResponse(saved);
    }

    // ✅ GET ALL SALES
    public List<SaleResponse> getAllSales() throws Exception {

        return firestoreRepository.findAll(SALE_COLLECTION, Sale.class)
                .stream()
                .map(SaleMapper::toResponse)
                .toList();
    }

    // ✅ GET SALE BY ID
    public SaleResponse getSaleById(String id) throws Exception {

        Sale sale = firestoreRepository.findById(SALE_COLLECTION, id, Sale.class);

        if (sale == null) {
            throw new RuntimeException("Sale not found");
        }

        return SaleMapper.toResponse(sale);
    }
}