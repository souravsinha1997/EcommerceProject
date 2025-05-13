package com.ecommerce.product_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ecommerce.product_service.dto.MessageResponse;
import com.ecommerce.product_service.dto.ProductRequest;
import com.ecommerce.product_service.dto.ProductResponse;
import com.ecommerce.product_service.service.ProductService;

@RestController
@RequestMapping("/api/v2/admin")
public class ProductAdminControllerV2 {

	private final ProductService productService;
	
	public ProductAdminControllerV2(ProductService productService) {
		this.productService = productService;
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping("/product")
	public ResponseEntity<MessageResponse> createProduct(@RequestBody ProductRequest request){
		return ResponseEntity.ok(new MessageResponse(productService.createProduct(request)));
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@DeleteMapping("/product/{id}")
	public ResponseEntity<MessageResponse> deleteProduct(@PathVariable int id){
		return ResponseEntity.ok(new MessageResponse(productService.deleteProduct(id)));
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@PutMapping("/product/{id}")
	public ResponseEntity<ProductResponse> updateProduct(@RequestBody ProductRequest request, @PathVariable int id){
		return ResponseEntity.ok(productService.updateProduct(request, id));
	}
}
