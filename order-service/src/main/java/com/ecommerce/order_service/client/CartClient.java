package com.ecommerce.order_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ecommerce.order_service.client.dto.CartResponse;
import com.ecommerce.order_service.config.FeignClientConfig;

@FeignClient(name = "CART-SERVICE", configuration = FeignClientConfig.class)
public interface CartClient {

	@GetMapping("/api/cart/{customerId}")
	public ResponseEntity<CartResponse> getAllItems(@PathVariable int customerId);
	
	@DeleteMapping("/api/cart/{customerId}")
	public ResponseEntity<String> clearCart(@PathVariable int customerId);
}
