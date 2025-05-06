package com.ecommerce.payment_service.exception;

public class InvalidPaymentIdException extends RuntimeException{

	public InvalidPaymentIdException(String message) {
		super(message);
	}
}
