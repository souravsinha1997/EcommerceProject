package com.ecommerce.customer_service.service;

import java.util.List;

import com.ecommerce.customer_service.dto.LoginRequest;
import com.ecommerce.customer_service.dto.UserRequest;
import com.ecommerce.customer_service.dto.UserResponse;
import com.ecommerce.customer_service.entity.Token;

public interface UserService {

	List<UserResponse> getAllUsers();
	UserResponse getUser(int id);
	String deleteUser(int id);
	UserResponse updateUser(UserRequest request, int id);
	String saveUser(UserRequest userRequest);
	Token loginUser(LoginRequest loginRequest);
}
