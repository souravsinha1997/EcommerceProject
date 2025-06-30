package com.ecommerce.order_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ecommerce.order_service.client.dto.UserResponse;



@FeignClient(name = "CUSTOMER-SERVICE")
public interface CustomerClient {
	
    @GetMapping("/api/customers/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable int id);

}
