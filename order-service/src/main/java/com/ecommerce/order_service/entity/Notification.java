package com.ecommerce.order_service.entity;

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
public class Notification {
	
	private String userName;
	private int orderId;
	private String usermail;
	private BigDecimal amount;
	private String status;
	
}
