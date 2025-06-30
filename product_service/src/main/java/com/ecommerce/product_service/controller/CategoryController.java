package com.ecommerce.product_service.controller;

import java.util.List;

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

import com.ecommerce.product_service.dto.CategoryRequest;
import com.ecommerce.product_service.dto.CategoryResponse;
import com.ecommerce.product_service.dto.MessageResponse;
import com.ecommerce.product_service.service.CategoryService;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

	private final CategoryService categoryService;
	
	public CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}
	
	@GetMapping("/all")
	public ResponseEntity<List<CategoryResponse>> getAllCategories(){
		return ResponseEntity.ok(categoryService.getAllCategories());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<CategoryResponse> getCategory(@PathVariable int id){
		return ResponseEntity.ok(categoryService.getCategory(id));
	}
	
	@PreAuthorize("hasAuthority('ADMIN")
	@PostMapping("/admin/category")
	public ResponseEntity<MessageResponse> createCategory(@RequestBody CategoryRequest request){
		return ResponseEntity.ok(new MessageResponse(categoryService.saveCategory(request)));
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@DeleteMapping("/admin/category/{id}")
	public ResponseEntity<MessageResponse> deleteCategory(@PathVariable int id){
		return ResponseEntity.ok(new MessageResponse(categoryService.deleteCategory(id)));
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@PutMapping("/admin/category/{id}")
	public ResponseEntity<CategoryResponse> updateCategory(@RequestBody CategoryRequest request,@PathVariable int id){
		return ResponseEntity.ok(categoryService.updateCategory(request, id));
	}
}
