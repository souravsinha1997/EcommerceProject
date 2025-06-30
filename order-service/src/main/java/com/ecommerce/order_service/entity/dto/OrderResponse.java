package com.ecommerce.order_service.entity.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
public class OrderResponse {

	private String customerName;
	private int orderId;
	private String status;
	private BigDecimal totalPrice;
	private LocalDateTime orderDate;
	private List<OrderItemResponse> items;
}
