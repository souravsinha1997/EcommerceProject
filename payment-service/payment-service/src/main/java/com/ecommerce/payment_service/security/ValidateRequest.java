package com.ecommerce.payment_service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ValidateRequest {
	
	@Autowired
	private JwtService jwtService;
	
	
	
    private String getAuthToken() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getCredentials().toString();
    }

    private int getCustomerId() {
        String token = getAuthToken();
        int customerId = jwtService.getCustomerId(token);
        return Integer.valueOf(customerId);
    }
    
    public  boolean validateCustomer(int customerId) {
    	int tokenId = getCustomerId();
    	return customerId==tokenId;
    }
}
