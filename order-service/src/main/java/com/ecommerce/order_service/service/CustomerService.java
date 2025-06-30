package com.ecommerce.order_service.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ecommerce.order_service.client.CustomerClient;
import com.ecommerce.order_service.client.dto.Address;
import com.ecommerce.order_service.client.dto.Role;
import com.ecommerce.order_service.client.dto.UserResponse;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class CustomerService {

	private final CustomerClient customerClient;
	
	public CustomerService(CustomerClient customerClient) {
		this.customerClient = customerClient;
	}
	
	@CircuitBreaker(name = "customerService", fallbackMethod = "fallbackCustomer")
    public ResponseEntity<UserResponse> fetchUser(int customerId) {
        return customerClient.getUser(customerId);
    }

    public ResponseEntity<UserResponse> fallbackCustomer(int customerId, Throwable t) {
    	Address addr = new Address();
        return ResponseEntity.ok(new UserResponse("Fallback", "Service Down","Service Down","Service Down","Service Down",Role.SERVERDOWN,addr));
    }
}
