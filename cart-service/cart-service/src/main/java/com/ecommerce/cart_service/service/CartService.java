package com.ecommerce.cart_service.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ecommerce.cart_service.client.ProductClient;
import com.ecommerce.cart_service.dto.CartItem;
import com.ecommerce.cart_service.dto.CartRequest;
import com.ecommerce.cart_service.dto.CartResponse;
import com.ecommerce.cart_service.dto.ProductResponse;

@Service
public class CartService {

	
	private final CartRedisService cartRedisService;
	private final ProductClient productClient;
	private final ValidateRequest validate;
	
	public CartService(ValidateRequest validate,CartRedisService cartRedisService,ProductClient productClient) {
		this.cartRedisService = cartRedisService;
		this.productClient = productClient;
		this.validate = validate;
	}
	
	public CartResponse getAllCartItems(int customerId) {
		if(!validate.validateCustomer(customerId)) {
			throw new RuntimeException("Invalid customer id in request");
		}
		String id = Integer.toString(customerId);
		List<CartItem> items = new ArrayList<>();
		CartResponse response = new CartResponse();
		response.setCustomerId(customerId);
		
		items = cartRedisService.getCart(id);
		response.setItems(items);
		
		return response;	
	}
	
	public String addItems(CartRequest request) {
		if(!validate.validateCustomer(request.getCustomerId())) {
			throw new RuntimeException("Invalid customer id in request");
		}		
		ResponseEntity<ProductResponse> product = productClient.getProductById(request.getProductId());
		
		CartItem item = new CartItem();
		item.setName(product.getBody().getName());
		item.setPrice(product.getBody().getPrice());
		item.setProductId(request.getProductId());
		
		if(product.getBody().getQuantity()<request.getQuantity() || request.getQuantity()<0) {
			throw new RuntimeException("Invalid Quantity");
		}
		else {
			item.setQuantity(request.getQuantity());
		}

		String customerId = Integer.toString(request.getCustomerId());
		cartRedisService.addToCart(customerId, item);
		return "Item added to the cart";
		
	}
	
	public CartResponse getCartItem(CartRequest request) {
		if(!validate.validateCustomer(request.getCustomerId())) {
			throw new RuntimeException("Invalid customer id in request");
		}
		validate.validateCustomer(request.getCustomerId());
		String custId = Integer.toString(request.getCustomerId());
		CartItem item= cartRedisService.getCartItemByProductId(custId, request.getProductId());
		CartResponse response = new CartResponse();
		List<CartItem> itemList = new ArrayList<>();
		itemList.add(item);
		response.setCustomerId(request.getCustomerId());
		response.setItems(itemList);
		return response;
	}
	
	public String updateCartItem(CartRequest request) {
		if(!validate.validateCustomer(request.getCustomerId())) {
			throw new RuntimeException("Invalid customer id in request");
		}
		validate.validateCustomer(request.getCustomerId());
		String custId = Integer.toString(request.getCustomerId());
		cartRedisService.updateCartItem(custId, request.getProductId(), request.getQuantity());		
		return "Cart updated successfully";
	}
	
	public String removeCartItem(CartRequest request) {
		if(!validate.validateCustomer(request.getCustomerId())) {
			throw new RuntimeException("Invalid customer id in request");
		}
		validate.validateCustomer(request.getCustomerId());
		String custId = Integer.toString(request.getCustomerId());
		cartRedisService.removeFromCart(custId, request.getProductId());
		return "Item removed successfully";
	}
	
	public String clearCartItems(int customerId) {
		if(!validate.validateCustomer(customerId)) {
			throw new RuntimeException("Invalid customer id in request");
		}
		validate.validateCustomer(customerId);
		String custId = Integer.toString(customerId);
		cartRedisService.clearCart(custId);
		return "All items are cleared";
	}
}
