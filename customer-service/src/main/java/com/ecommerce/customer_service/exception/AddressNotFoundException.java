package com.ecommerce.customer_service.exception;

public class AddressNotFoundException extends RuntimeException{

	public AddressNotFoundException(String message) {
		super(message);
	}
}
