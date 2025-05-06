package com.ecommerce.payment_service.dto;

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
