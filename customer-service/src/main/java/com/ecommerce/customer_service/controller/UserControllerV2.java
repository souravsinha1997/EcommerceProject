package com.ecommerce.customer_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.customer_service.dto.LoginRequest;
import com.ecommerce.customer_service.dto.MessageResponse;
import com.ecommerce.customer_service.dto.UserRequest;
import com.ecommerce.customer_service.dto.UserResponse;
import com.ecommerce.customer_service.entity.Token;
import com.ecommerce.customer_service.service.UserService;

@RestController
@RequestMapping("/api/v2/customers")
public class UserControllerV2 {

	private final UserService userService;
	
	public UserControllerV2(UserService userService) {
		this.userService=userService;
	}
	
	@PostMapping("/signIn")
    public ResponseEntity<Token> loginUser(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(userService.loginUser(loginRequest));
    }

    @PostMapping("/signUp")
    public ResponseEntity<MessageResponse> createUser(@RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(new MessageResponse(userService.saveUser(userRequest)));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable int id) {
        return ResponseEntity.ok(userService.getUser(id));
    }
    
    
}
