package com.ecommerce.cart_service.controller;

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
	public ResponseEntity<String> addItem(@RequestBody CartRequest request){
		System.out.println("Inside controller");
		return ResponseEntity.ok(cartService.addItems(request));
	}
	
	@GetMapping("/product")
	public ResponseEntity<CartResponse> getItem(@RequestBody CartRequest request){
		return ResponseEntity.ok(cartService.getCartItem(request));
	}
	
	@PutMapping("/update")
	public ResponseEntity<String> updateItem(@RequestBody CartRequest request){
		return ResponseEntity.ok(cartService.updateCartItem(request));
	}
	
	@DeleteMapping("/remove")
	public ResponseEntity<String> removeItem(@RequestBody CartRequest request){
		return ResponseEntity.ok(cartService.removeCartItem(request));
	}
	
	@DeleteMapping("/{customerId}")
	public ResponseEntity<String> clearCart(@PathVariable int customerId){
		return ResponseEntity.ok(cartService.clearCartItems(customerId));
	}
}
