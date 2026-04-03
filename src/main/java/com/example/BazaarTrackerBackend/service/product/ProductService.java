package com.example.BazaarTrackerBackend.service.product;

import com.example.BazaarTrackerBackend.dto.product.ProductRequest;
import com.example.BazaarTrackerBackend.dto.product.ProductResponse;
import com.example.BazaarTrackerBackend.exception.CustomException;
import com.example.BazaarTrackerBackend.mapper.ProductMapper;
import com.example.BazaarTrackerBackend.model.entity.Product;
import com.example.BazaarTrackerBackend.repository.firestore.FirestoreRepository;
import com.example.BazaarTrackerBackend.constants.CollectionNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import com.google.cloud.Timestamp;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private FirestoreRepository firestoreRepository;

    private static final String COLLECTION = CollectionNames.PRODUCTS;

    private String getCurrentUserId() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public ProductResponse createProduct(ProductRequest request) throws Exception {

        Product product = ProductMapper.toEntity(request);

        product.setUserId(getCurrentUserId());
        product.setStock(request.getStock());
        product.setActive(true);
        product.setCreatedAt(Timestamp.now());
        product.setUpdatedAt(Timestamp.now());

        Product saved = firestoreRepository.save(COLLECTION, product);

        return ProductMapper.toResponse(saved);
    }

    public List<ProductResponse> getAllProducts() throws Exception {
        return firestoreRepository.findByField(COLLECTION, "userId", getCurrentUserId(), Product.class)
                .stream()
                .map(ProductMapper::toResponse)
                .toList();
    }

    public ProductResponse getProductById(String id) throws Exception {
        Product product = getProductEntity(id);
        return ProductMapper.toResponse(product);
    }

    public void reduceStock(String productId, int quantity) throws Exception {
        Product product = getProductEntity(productId);
        if (product.getStock() < quantity) {
            throw new CustomException("Insufficient stock for product: " + product.getName(), 400);
        }
        product.setStock(product.getStock() - quantity);
        saveProduct(product);
    }

    public void increaseStock(String productId, int quantity) throws Exception {
        Product product = getProductEntity(productId);
        product.setStock(product.getStock() + quantity);
        saveProduct(product);
    }

    private Product getProductEntity(String productId) throws Exception {
        Product product = firestoreRepository.findById(COLLECTION, productId, Product.class);
        if (product == null) {
            throw new CustomException("Product not found", 404);
        }
        if (product.getUserId() == null || !product.getUserId().equals(getCurrentUserId())) {
            throw new CustomException("Unauthorized access", 403);
        }
        return product;
    }

    private void saveProduct(Product product) throws Exception {
        product.setUpdatedAt(Timestamp.now());
        firestoreRepository.save(COLLECTION, product);
    }
}
