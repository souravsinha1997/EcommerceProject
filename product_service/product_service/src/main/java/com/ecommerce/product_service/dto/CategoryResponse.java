package com.ecommerce.product_service.dto;

import java.util.List;

import com.ecommerce.product_service.entity.Category;
import com.ecommerce.product_service.entity.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CategoryResponse {

	private String name;
	private String description;
	
}
