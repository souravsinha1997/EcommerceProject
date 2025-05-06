package com.ecommerce.payment_service.exception;

public class InvalidTokenException extends RuntimeException {

	public InvalidTokenException(String message) {
		super(message);
	}
}
