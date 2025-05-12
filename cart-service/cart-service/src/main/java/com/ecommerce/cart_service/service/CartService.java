package com.ecommerce.cart_service.service;

import com.ecommerce.cart_service.dto.CartRequest;
import com.ecommerce.cart_service.dto.CartResponse;

public interface CartService {

	CartResponse getAllCartItems(int customerId);
	String addItems(int customerId,CartRequest request);
	CartResponse getCartItem(int customerId,CartRequest request);
	String updateCartItem(int customerId,CartRequest request);
	String removeCartItem(int customerId,CartRequest request);
	String clearCartItems(int customerId);
}
