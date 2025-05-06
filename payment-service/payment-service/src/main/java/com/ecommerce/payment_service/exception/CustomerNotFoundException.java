package com.ecommerce.payment_service.exception;

public class CustomerNotFoundException extends RuntimeException {
	public CustomerNotFoundException(String message) {
		super(message);
	}
}
