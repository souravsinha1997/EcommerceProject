package com.ecommerce.payment_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ecommerce.payment_service.client.dto.UserResponse;
import com.ecommerce.payment_service.config.FeignClientConfig;



@FeignClient(name = "CUSTOMER-SERVICE", configuration = FeignClientConfig.class)
public interface CustomerClient {
	
    @GetMapping("/api/customers/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable int id);

}
