package com.ecommerce.order_service.entity.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
public class PaymentResponse {

	private Integer id;
    private Integer userId;
    private Integer orderId;
    private BigDecimal amount;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime paidAt;
}
