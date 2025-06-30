package com.ecommerce.product_service.service;

import java.util.List;

import com.ecommerce.product_service.dto.CategoryRequest;
import com.ecommerce.product_service.dto.CategoryResponse;

public interface CategoryService {
	List<CategoryResponse> getAllCategories();
	CategoryResponse getCategory(int id);
	String saveCategory(CategoryRequest request);
	String deleteCategory(int id);
	CategoryResponse updateCategory(CategoryRequest request,int id);
}
