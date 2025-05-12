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
@RequestMapping("/api/v2/admin")
public class AdminControllerV2 {

	private final UserService userService;
	
	public AdminControllerV2(UserService userService) {
		this.userService=userService;
	}

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/customers")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
    
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/customers/{id}")
    public ResponseEntity<UserResponse> updateUser(@RequestBody UserRequest userRequest,@PathVariable int id) {
        return ResponseEntity.ok(userService.updateUser(userRequest, id));
    }
    
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/customers/{id}")
    public ResponseEntity<MessageResponse> deleteUser(@PathVariable int id) {
        return ResponseEntity.ok(new MessageResponse(userService.deleteUser(id)));
    }
    
    
}
