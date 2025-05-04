package com.ecommerce.product_service.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.product_service.dto.ProductRequest;
import com.ecommerce.product_service.dto.ProductResponse;
import com.ecommerce.product_service.entity.Category;
import com.ecommerce.product_service.entity.Product;
import com.ecommerce.product_service.repository.CategoryRepository;
import com.ecommerce.product_service.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepo;
	private final CategoryRepository categoryRepo;
	
	public ProductServiceImpl(ProductRepository productRepo, CategoryRepository categoryRepo) {
		this.categoryRepo = categoryRepo;
		this.productRepo = productRepo;
	}
	
	public List<ProductResponse> getAllProducts(){
		List<Product> products = productRepo.findAll();
		List<ProductResponse> response = new ArrayList<>();
		
		if(products==null) {
			throw new RuntimeException("No products found");
		}
		for(Product product : products) {
			ProductResponse res = EntityToResponse(product);
			response.add(res);
		}
		
		return response;
	}
	
	
	public ProductResponse getProduct(int id) {
		Product product = productRepo.findById(id).orElseThrow(()-> new RuntimeException("No products found"));
		
		ProductResponse response = EntityToResponse(product);
		return response;
	}
	
	public ProductResponse getProductByName(String name) {
		Product product = productRepo.findByName(name).orElseThrow(()-> new RuntimeException("No products found"));
		
		ProductResponse response = EntityToResponse(product);
		return response;
	}
	
	@Transactional
	public List<ProductResponse> getProductByCategory(String category) {
		Category savedCategory = categoryRepo.findByName(category);
		List<Product> products = productRepo.findByCategory(savedCategory);//.orElseThrow(()-> new RuntimeException("No products found"));
		
		List<ProductResponse> responseList = new ArrayList<>();
		for(Product product : products) {
			ProductResponse response = EntityToResponse(product);
			responseList.add(response);
		}
		
		return responseList;
	}
	
	@Transactional
	public String createProduct(ProductRequest request) {
		
		Category category = categoryRepo.findByName(request.getCategory());
		Product product = RequestToEntity(request);
		product.setCategory(category);
		
		productRepo.save(product);
		return "Product saved successfully";
	}
	
	public String deleteProduct(int id) {
		Product product = productRepo.findById(id)
							.orElseThrow(() -> new RuntimeException("Product not found"));
		
		productRepo.delete(product);
		return "Product deleted successfully";
	}
	
	public ProductResponse updateProduct(ProductRequest request, int id) {
		Product savedProduct = productRepo.findById(id)
								.orElseThrow(()-> new RuntimeException("No products found"));
		
		if(request.getBrand()!=null && !request.getBrand().isEmpty()) {
			savedProduct.setBrand(request.getBrand());
		}
		if(request.getName()!=null && !request.getName().isEmpty()) {
			savedProduct.setName(request.getName());
		}
		if(!(request.getPrice()<=0)) {
			savedProduct.setPrice(request.getPrice());
		}
		if(!(request.getQuantity()<0)) {
			savedProduct.setQuantity(request.getQuantity());
		}
		
		Product updatedProduct = productRepo.save(savedProduct);
		
		return EntityToResponse(updatedProduct);
	}
	
	public static ProductResponse EntityToResponse(Product product) {
		ProductResponse response = new ProductResponse();
		response.setName(product.getName());
		response.setBrand(product.getBrand());
		response.setPrice(product.getPrice());
		response.setQuantity(product.getQuantity());
		response.setCategory(product.getCategory().getName());
		
		return response;
	}
	
	public static Product RequestToEntity(ProductRequest request) {
		Product product = new Product();
		product.setBrand(request.getBrand());
		product.setName(request.getName());
		product.setPrice(request.getPrice());
		product.setQuantity(request.getQuantity());
		
		return product;
	}
}
