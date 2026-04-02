package com.example.BazaarTrackerBackend.service.product;

import com.example.BazaarTrackerBackend.dto.product.ProductRequest;
import com.example.BazaarTrackerBackend.dto.product.ProductResponse;

import com.example.BazaarTrackerBackend.mapper.ProductMapper;

import com.example.BazaarTrackerBackend.model.entity.Product;

import com.example.BazaarTrackerBackend.repository.firestore.FirestoreRepository;

import com.example.BazaarTrackerBackend.constants.CollectionNames;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private FirestoreRepository firestoreRepository;

    private static final String COLLECTION = CollectionNames.PRODUCTS;

    // ✅ CREATE PRODUCT
    public ProductResponse createProduct(ProductRequest request) throws Exception {

        Product product = ProductMapper.toEntity(request);

        product.setStock(request.getStock()); // initial stock
        product.setActive(true);

        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        Product saved = firestoreRepository.save(COLLECTION, product);

        return ProductMapper.toResponse(saved);
    }

    // ✅ GET ALL PRODUCTS
    public List<ProductResponse> getAllProducts() throws Exception {

        return firestoreRepository.findAll(COLLECTION, Product.class)
                .stream()
                .map(ProductMapper::toResponse)
                .toList();
    }

    // ✅ GET PRODUCT BY ID
    public ProductResponse getProductById(String id) throws Exception {

        Product product = firestoreRepository.findById(COLLECTION, id, Product.class);

        if (product == null) {
            throw new RuntimeException("Product not found");
        }

        return ProductMapper.toResponse(product);
    }

    // 🔥 DECREASE STOCK (Used in SalesService)
    public void reduceStock(String productId, int quantity) throws Exception {

        Product product = getProductEntity(productId);

        // ⚠️ Validation
        if (product.getStock() < quantity) {
            throw new RuntimeException("Insufficient stock for product: " + product.getName());
        }

        product.setStock(product.getStock() - quantity);

        saveProduct(product);
    }

    // 🔥 INCREASE STOCK (Future use / restock)
    public void increaseStock(String productId, int quantity) throws Exception {

        Product product = getProductEntity(productId);

        product.setStock(product.getStock() + quantity);

        saveProduct(product);
    }

    // 🔒 INTERNAL: Fetch entity
    private Product getProductEntity(String productId) throws Exception {

        Product product = firestoreRepository.findById(COLLECTION, productId, Product.class);

        if (product == null) {
            throw new RuntimeException("Product not found");
        }

        return product;
    }

    // 🔒 INTERNAL: Save product
    private void saveProduct(Product product) throws Exception {

        product.setUpdatedAt(LocalDateTime.now());

        firestoreRepository.save(COLLECTION, product);
    }
}