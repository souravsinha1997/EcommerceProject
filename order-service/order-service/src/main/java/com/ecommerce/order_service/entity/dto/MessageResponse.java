package com.ecommerce.order_service.entity.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageResponse {

	String message;
	
	public MessageResponse(String message) {
		this.message = message;
	}
}
