package com.ecommerce.product_service.service;

import java.util.List;

import com.ecommerce.product_service.dto.ProductRequest;
import com.ecommerce.product_service.dto.ProductResponse;

public interface ProductService {
	List<ProductResponse> getAllProducts();
	ProductResponse getProduct(int id);
	ProductResponse getProductByName(String name);
	List<ProductResponse> getProductByCategory(String category);
	String createProduct(ProductRequest request);
	String deleteProduct(int id);
	ProductResponse updateProduct(ProductRequest request, int id);
	
}
