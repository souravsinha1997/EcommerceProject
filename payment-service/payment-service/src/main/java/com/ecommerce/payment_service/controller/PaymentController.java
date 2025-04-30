package com.ecommerce.payment_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.payment_service.service.PaymentService;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

	
	private final PaymentService paymentService;
	
	public PaymentController(PaymentService paymentService) {
		this.paymentService = paymentService;
	}
	
	@PreAuthorize("hasAuthority('ROLE_USER')")
	@PostMapping("/{paymentId}")
	public ResponseEntity<String> completePayment(@PathVariable int paymentId) {
		return ResponseEntity.ok(paymentService.payTheAmount(paymentId));
	}
}
