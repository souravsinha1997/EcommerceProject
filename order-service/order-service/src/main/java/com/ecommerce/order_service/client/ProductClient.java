package com.ecommerce.order_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ecommerce.order_service.client.dto.ProductRequest;
import com.ecommerce.order_service.client.dto.ProductResponse;



@FeignClient(name = "PRODUCT-SERVICE")
public interface ProductClient {

	@GetMapping("/api/products/{id}")
	ResponseEntity<ProductResponse> getProductById(@PathVariable int id);
	
	@PutMapping("/api/products/stock-update/{id}")
	public ResponseEntity<ProductResponse> updateStock(@RequestBody ProductRequest request, @PathVariable int id);
		
}
