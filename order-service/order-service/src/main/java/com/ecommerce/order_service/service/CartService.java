package com.ecommerce.order_service.service;

import java.util.ArrayList;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ecommerce.order_service.client.CartClient;
import com.ecommerce.order_service.client.dto.CartItem;
import com.ecommerce.order_service.client.dto.CartResponse;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class CartService {
	
	private final CartClient cartClient;
	
	public CartService(CartClient cartClient) {
		this.cartClient = cartClient;
	}
	
	@CircuitBreaker(name = "cartService", fallbackMethod = "fallbackCart")
	public ResponseEntity<CartResponse> fetchAllItems(int customerId){
		return cartClient.getAllItems(customerId);
	}
	
	public ResponseEntity<CartResponse> fallbackCart(int customerId, Throwable t){
		return ResponseEntity.ok(new CartResponse(0, new ArrayList<CartItem>()));
	}
	
	@CircuitBreaker(name = "cartService", fallbackMethod = "fallbackClearCart")
	public ResponseEntity<String> clearCart(int customerId){
		return cartClient.clearCart(customerId);
	}
	
	public ResponseEntity<String> fallbackClearCart(int customerId, Throwable t){
		return ResponseEntity.ok("Fallback");
	}
}
