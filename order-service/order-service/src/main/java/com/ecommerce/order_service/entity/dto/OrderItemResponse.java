package com.ecommerce.order_service.entity.dto;

import java.math.BigDecimal;

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
public class OrderItemResponse {

	private String productName;
	private int quantity;
	private BigDecimal price;
}
