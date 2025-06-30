package com.ecommerce.product_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.product_service.entity.Category;
import com.ecommerce.product_service.entity.Product;

public interface ProductRepository extends JpaRepository<Product,Integer>{

	List<Product> findByCategory(Category category);

	Optional<Product> findByName(String name);

}
