package com.ecommerce.customer_service.dto;

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
