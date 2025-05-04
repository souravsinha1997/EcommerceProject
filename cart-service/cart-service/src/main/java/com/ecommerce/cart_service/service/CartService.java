package com.ecommerce.cart_service.service;

import com.ecommerce.cart_service.dto.CartRequest;
import com.ecommerce.cart_service.dto.CartResponse;

public interface CartService {

	CartResponse getAllCartItems(int customerId);
	String addItems(CartRequest request);
	CartResponse getCartItem(CartRequest request);
	String updateCartItem(CartRequest request);
	String removeCartItem(CartRequest request);
	String clearCartItems(int customerId);
}
