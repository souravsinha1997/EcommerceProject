package com.ecommerce.cart_service.exception;

public class EmptyCartException extends RuntimeException {

	public EmptyCartException(String message) {
		super(message);
	}
}
