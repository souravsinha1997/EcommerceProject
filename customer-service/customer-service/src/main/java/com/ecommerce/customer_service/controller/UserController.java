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
import com.ecommerce.customer_service.dto.UserRequest;
import com.ecommerce.customer_service.dto.UserResponse;
import com.ecommerce.customer_service.entity.Token;
import com.ecommerce.customer_service.service.UserService;

@RestController
@RequestMapping("/api/customers")
public class UserController {

	private final UserService userService;
	
	public UserController(UserService userService) {
		this.userService=userService;
	}
	
	@PostMapping("/login")
    public ResponseEntity<Token> loginUser(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(userService.loginUser(loginRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<String> createUser(@RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(userService.saveUser(userRequest));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/admin/customers")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable int id) {
        return ResponseEntity.ok(userService.getUser(id));
    }
    
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/admin/customers/{id}")
    public ResponseEntity<UserResponse> updateUser(@RequestBody UserRequest userRequest,@PathVariable int id) {
        return ResponseEntity.ok(userService.updateUser(userRequest, id));
    }
    
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/admin/customers/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id) {
        return ResponseEntity.ok(userService.deleteUser(id));
    }
    
    
}
