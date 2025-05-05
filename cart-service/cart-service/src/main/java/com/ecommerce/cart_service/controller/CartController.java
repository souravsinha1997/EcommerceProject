package com.ecommerce.cart_service.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.cart_service.dto.CartRequest;
import com.ecommerce.cart_service.dto.CartResponse;
import com.ecommerce.cart_service.service.CartService;

import ch.qos.logback.core.net.SyslogOutputStream;

@RestController
@RequestMapping("/api/cart")
public class CartController {

	private final CartService cartService;
	
	public CartController(CartService cartService) {
		this.cartService = cartService;
	}
	
	@GetMapping("/{customerId}")
	public ResponseEntity<CartResponse> getAllItems(@PathVariable int customerId) {
		return ResponseEntity.ok(cartService.getAllCartItems(customerId));
	}
	
	@PostMapping("/add")
	public ResponseEntity<Map<String,String>> addItem(@RequestBody CartRequest request){
		Map<String,String> response = new HashMap<>();
		response.put("message", cartService.addItems(request));
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/product")
	public ResponseEntity<CartResponse> getItem(@RequestBody CartRequest request){
		return ResponseEntity.ok(cartService.getCartItem(request));
	}
	
	@PutMapping("/update")
	public ResponseEntity<Map<String,String>> updateItem(@RequestBody CartRequest request){
		Map<String,String> response = new HashMap<>();
		response.put("message", cartService.updateCartItem(request));
		return ResponseEntity.ok(response);
	}
	
	@DeleteMapping("/remove")
	public ResponseEntity<Map<String,String>> removeItem(@RequestBody CartRequest request){
		Map<String,String> response = new HashMap<>();
		response.put("message", cartService.removeCartItem(request));
		return ResponseEntity.ok(response);
	}
	
	@DeleteMapping("/{customerId}")
	public ResponseEntity<Map<String,String>> clearCart(@PathVariable int customerId){
		Map<String,String> response = new HashMap<>();
		response.put("message", cartService.clearCartItems(customerId));
		return ResponseEntity.ok(response);
	}
}
