package com.example.BazaarTrackerBackend.service.sales;

import com.example.BazaarTrackerBackend.dto.sales.SaleRequest;
import com.example.BazaarTrackerBackend.dto.sales.SaleResponse;
import com.example.BazaarTrackerBackend.exception.CustomException;
import com.example.BazaarTrackerBackend.mapper.SaleMapper;
import com.example.BazaarTrackerBackend.model.entity.Product;
import com.example.BazaarTrackerBackend.model.entity.Sale;
import com.example.BazaarTrackerBackend.model.enums.SaleType;
import com.example.BazaarTrackerBackend.repository.firestore.FirestoreRepository;
import com.example.BazaarTrackerBackend.constants.CollectionNames;
import com.example.BazaarTrackerBackend.service.product.ProductService;
import com.example.BazaarTrackerBackend.service.vendor.VendorService;
import com.example.BazaarTrackerBackend.service.dashboard.DashboardService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import com.google.cloud.Timestamp;
import java.util.List;

@Service
public class SalesService {

    @Autowired
    private FirestoreRepository firestoreRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private VendorService vendorService;

    @Autowired
    private DashboardService dashboardService;

    private static final String SALE_COLLECTION = CollectionNames.SALES;

    private String getCurrentUserId() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public SaleResponse createSale(SaleRequest request) throws Exception {

        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new CustomException("Sale must contain at least one item", 400);
        }

        double totalAmount = 0;
        String userId = getCurrentUserId();

        for (var item : request.getItems()) {
            Product product = firestoreRepository.findById(
                    CollectionNames.PRODUCTS,
                    item.getProductId(),
                    Product.class
            );

            if (product == null || !userId.equals(product.getUserId())) {
                throw new CustomException("Product not found or unauthorized: " + item.getProductId(), 404);
            }

            if (product.getStock() < item.getQuantity()) {
                throw new CustomException("Insufficient stock for: " + product.getName(), 400);
            }

            double itemTotal = product.getPrice() * item.getQuantity();
            totalAmount += itemTotal;
        }

        for (var item : request.getItems()) {
            productService.reduceStock(item.getProductId(), item.getQuantity());
        }

        if (request.getSaleType() == SaleType.CREDIT) {
            if (request.getVendorId() == null) {
                throw new CustomException("Vendor required for credit sale", 400);
            }
            vendorService.addCredit(request.getVendorId(), totalAmount);
        }

        Sale sale = SaleMapper.toEntity(request);

        sale.setUserId(userId);
        sale.setTotalAmount(totalAmount);
        sale.setCreatedAt(Timestamp.now());
        sale.setUpdatedAt(Timestamp.now());

        Sale saved = firestoreRepository.save(SALE_COLLECTION, sale);

        // Update Dashboard appropriately
        dashboardService.recordSale(userId, totalAmount, request.getSaleType());

        return SaleMapper.toResponse(saved);
    }

    public List<SaleResponse> getAllSales() throws Exception {
        return firestoreRepository.findByField(SALE_COLLECTION, "userId", getCurrentUserId(), Sale.class)
                .stream()
                .map(SaleMapper::toResponse)
                .toList();
    }

    public SaleResponse getSaleById(String id) throws Exception {
        Sale sale = firestoreRepository.findById(SALE_COLLECTION, id, Sale.class);

        if (sale == null) {
            throw new CustomException("Sale not found", 404);
        }

        if (sale.getUserId() == null || !sale.getUserId().equals(getCurrentUserId())) {
            throw new CustomException("Unauthorized access", 403);
        }

        return SaleMapper.toResponse(sale);
    }
}
