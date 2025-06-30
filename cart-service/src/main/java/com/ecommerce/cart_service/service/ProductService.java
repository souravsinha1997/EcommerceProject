package com.ecommerce.cart_service.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ecommerce.cart_service.client.ProductClient;
import com.ecommerce.cart_service.dto.ProductResponse;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class ProductService {

    private final ProductClient productClient;

    public ProductService(ProductClient productClient) {
        this.productClient = productClient;
    }

    @CircuitBreaker(name = "productService", fallbackMethod = "fallbackProduct")
    public ResponseEntity<ProductResponse> fetchProductDetails(int productId) {
        return productClient.getProductById(productId);
    }

    public ResponseEntity<ProductResponse> fallbackProduct(int productId, Throwable t) {
        return ResponseEntity.ok(new ProductResponse("Fallback", "Service Down", 0, 0, "Fallback"));
    }
}
