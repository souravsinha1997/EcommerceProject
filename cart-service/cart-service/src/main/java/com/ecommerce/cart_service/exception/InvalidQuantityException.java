package com.ecommerce.cart_service.exception;

public class InvalidQuantityException extends RuntimeException {

	public InvalidQuantityException(String message) {
		super(message);
	}
}
