package com.ecommerce.product_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.ecommerce.product_service.dto.MessageResponse;
import com.ecommerce.product_service.dto.ProductRequest;
import com.ecommerce.product_service.dto.ProductResponse;
import com.ecommerce.product_service.service.ProductService;

@RestController
@RequestMapping("/api/v2/products")
public class ProductControllerV2 {

	private final ProductService productService;
	
	public ProductControllerV2(ProductService productService) {
		this.productService = productService;
	}
	
	
//	@GetMapping
//	public ResponseEntity<List<ProductResponse>> getAllProducts(){
//		return ResponseEntity.ok(productService.getAllProducts());
//	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ProductResponse> getProduct(@PathVariable int id){
		return ResponseEntity.ok(productService.getProduct(id));
		//throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Simulated failure from Product Service");
	}
	
	@GetMapping
	public ResponseEntity<?> getProductByName(@RequestParam(required = false) String name){
		if(name!=null && !name.isEmpty())
			return ResponseEntity.ok(productService.getProductByName(name));
		else 
			return ResponseEntity.ok(productService.getAllProducts());
	}
	
	@GetMapping("/category/{category}")
	public ResponseEntity<List<ProductResponse>> getProduct(@PathVariable String category){
		return ResponseEntity.ok(productService.getProductByCategory(category));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ProductResponse> updateStock(@RequestBody ProductRequest request, @PathVariable int id){
		return ResponseEntity.ok(productService.updateProduct(request, id));
	}
}
