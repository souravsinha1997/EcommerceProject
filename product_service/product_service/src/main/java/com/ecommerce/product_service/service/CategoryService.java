package com.ecommerce.product_service.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.product_service.dto.CategoryRequest;
import com.ecommerce.product_service.dto.CategoryResponse;
import com.ecommerce.product_service.entity.Category;
import com.ecommerce.product_service.entity.Product;
import com.ecommerce.product_service.repository.CategoryRepository;
import com.ecommerce.product_service.repository.ProductRepository;

@Service
public class CategoryService {

	private final CategoryRepository categoryRepo;
	private final ProductRepository productRepo;
	
	public CategoryService(CategoryRepository categoryRepo,ProductRepository productRepo) {
		this.categoryRepo = categoryRepo;
		this.productRepo = productRepo;
	}
	
	public List<CategoryResponse> getAllCategories(){
		List<Category> categories = categoryRepo.findAll();
		
		if(categories==null) {
			throw new RuntimeException("No Category was found");
		}
		
		List<CategoryResponse> categoryResponse = new ArrayList<>();
		for(Category category : categories) {
			CategoryResponse response = new CategoryResponse();
			response.setName(category.getName());
			response.setDescription(category.getDescription());
			categoryResponse.add(response);
		}
		return categoryResponse;
	}
	
	public CategoryResponse getCategory(int id) {
		Category category = categoryRepo.findById(id).orElseThrow(()-> new RuntimeException("Category not found"));
		
		CategoryResponse response = new CategoryResponse();
		response.setName(category.getName());
		response.setDescription(category.getDescription());
		
		return response;
	}
	
	public String saveCategory(CategoryRequest request) {
		
		Category category = new Category();
		category.setDescription(request.getDescription());
		category.setName(request.getName());
		
		Category savedCategory = categoryRepo.save(category);
		
		return "Category Saved Successfully";
	}
	
	@Transactional
	public String deleteCategory(int id) {
		Category savedCategory = categoryRepo.findById(id).orElseThrow(()-> new RuntimeException("Category does not exist"));
		
		List<Product> products = productRepo.findByCategory(savedCategory);
		for(Product product : products) {
			product.setCategory(null);
		}
		
		categoryRepo.delete(savedCategory);
	
		return "Category Deleted Successfully";
	}
	
	public CategoryResponse updateCategory(CategoryRequest request,int id) {
		
		Category savedCategory = categoryRepo.findById(id)
				.orElseThrow(()-> new RuntimeException("Category does not exist"));
		
		if(request.getDescription()!=null && !request.getDescription().isEmpty())
			savedCategory.setDescription(request.getDescription());
		
		if(request.getName()!=null && !request.getName().isEmpty())
			savedCategory.setName(request.getName());
		
		Category updatedCategory = categoryRepo.save(savedCategory);
		
		CategoryResponse response = new CategoryResponse();
		response.setDescription(updatedCategory.getDescription());
		response.setName(updatedCategory.getName());
		
		return response;
		
	}
}
