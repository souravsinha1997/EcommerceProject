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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.ecommerce.product_service.dto.MessageResponse;
import com.ecommerce.product_service.dto.ProductRequest;
import com.ecommerce.product_service.dto.ProductResponse;
import com.ecommerce.product_service.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {

	private final ProductService productService;
	
	public ProductController(ProductService productService) {
		this.productService = productService;
	}
	
	
	@GetMapping("/all")
	public ResponseEntity<List<ProductResponse>> getAllProducts(){
		return ResponseEntity.ok(productService.getAllProducts());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ProductResponse> getProduct(@PathVariable int id){
		return ResponseEntity.ok(productService.getProduct(id));
		//throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Simulated failure from Product Service");
	}
	
	@GetMapping("/product/{name}")
	public ResponseEntity<ProductResponse> getProductByName(@PathVariable String name){
		return ResponseEntity.ok(productService.getProductByName(name));
	}
	
	@GetMapping("/category/{category}")
	public ResponseEntity<List<ProductResponse>> getProduct(@PathVariable String category){
		return ResponseEntity.ok(productService.getProductByCategory(category));
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping("/admin/product")
	public ResponseEntity<MessageResponse> createProduct(@RequestBody ProductRequest request){
		return ResponseEntity.ok(new MessageResponse(productService.createProduct(request)));
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@DeleteMapping("/admin/product/{id}")
	public ResponseEntity<MessageResponse> deleteProduct(@PathVariable int id){
		return ResponseEntity.ok(new MessageResponse(productService.deleteProduct(id)));
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@PutMapping("/admin/product/{id}")
	public ResponseEntity<ProductResponse> updateProduct(@RequestBody ProductRequest request, @PathVariable int id){
		return ResponseEntity.ok(productService.updateProduct(request, id));
	}
	
	@PutMapping("/stock-update/{id}")
	public ResponseEntity<ProductResponse> updateStock(@RequestBody ProductRequest request, @PathVariable int id){
		return ResponseEntity.ok(productService.updateProduct(request, id));
	}
}
