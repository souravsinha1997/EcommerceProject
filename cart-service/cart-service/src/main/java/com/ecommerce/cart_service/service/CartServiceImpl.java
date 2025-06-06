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
import com.ecommerce.cart_service.exception.ClientDownException;
import com.ecommerce.cart_service.exception.EmptyCartException;
import com.ecommerce.cart_service.exception.InvalidQuantityException;
import com.ecommerce.cart_service.exception.InvalidRequestException;
import com.ecommerce.cart_service.security.ValidateRequest;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class CartServiceImpl implements CartService{

	
	private final CartRedisService cartRedisService;
	private final ValidateRequest validate;
	private final ProductService productService;
	
	public CartServiceImpl(ProductService productService,ValidateRequest validate,CartRedisService cartRedisService) {
		this.cartRedisService = cartRedisService;
		this.validate = validate;
		this.productService = productService;
	}
	
	public CartResponse getAllCartItems(int customerId) {
		if(!validate.validateCustomer(customerId)) {
			throw new InvalidRequestException("Invalid Token");
		}
		String id = Integer.toString(customerId);
		List<CartItem> items = new ArrayList<>();
		CartResponse response = new CartResponse();
		response.setCustomerId(customerId);
		
		items = cartRedisService.getCart(id);
		if(items.isEmpty()) {
			throw new EmptyCartException("Cart is empty");
		}
		response.setItems(items);
		
		return response;	
	}
	
	public String addItems(int customerId,CartRequest request) {
		if(!validate.validateCustomer(customerId)) {
			throw new InvalidRequestException("Invalid Token");
		}		
		ResponseEntity<ProductResponse> product = productService.fetchProductDetails(request.getProductId());
		if(product.getBody().getCategory().equals("Fallback")) {
			throw new ClientDownException("Server is not available at this moment, please try after sometime!");
		}
		CartItem item = new CartItem();
		item.setName(product.getBody().getName());
		item.setPrice(product.getBody().getPrice());
		item.setProductId(request.getProductId());
		
		if(product.getBody().getQuantity()<request.getQuantity() || request.getQuantity()<=0) {
			throw new InvalidQuantityException("Invalid Quantity");
		}
		else {
			item.setQuantity(request.getQuantity());
		}

		String stringCustomerId = Integer.toString(customerId);
		cartRedisService.addToCart(stringCustomerId, item);
		return "Item added to the cart";
		
	}
	
	public CartResponse getCartItem(int customerId,CartRequest request) {
		if(!validate.validateCustomer(customerId)) {
			throw new InvalidRequestException("Invalid Token");
		}
		validate.validateCustomer(customerId);
		String custId = Integer.toString(customerId);
		CartItem item= cartRedisService.getCartItemByProductId(custId, request.getProductId());
		if(item == null) {
			throw new EmptyCartException("Product is not there in the cart");
		}
		CartResponse response = new CartResponse();
		List<CartItem> itemList = new ArrayList<>();
		itemList.add(item);
		response.setCustomerId(customerId);
		response.setItems(itemList);
		return response;
	}
	
	public String updateCartItem(int customerId,CartRequest request) {
		if(!validate.validateCustomer(customerId)) {
			throw new InvalidRequestException("Invalid Token");
		}
		validate.validateCustomer(customerId);
		String custId = Integer.toString(customerId);
		cartRedisService.updateCartItem(custId, request.getProductId(), request.getQuantity());		
		return "Cart updated successfully";
	}
	
	public String removeCartItem(int customerId,CartRequest request) {
		if(!validate.validateCustomer(customerId)) {
			throw new InvalidRequestException("Invalid Token");
		}
		validate.validateCustomer(customerId);
		String custId = Integer.toString(customerId);
		cartRedisService.removeFromCart(custId, request.getProductId());
		return "Item removed successfully";
	}
	
	public String clearCartItems(int customerId) {
		if(!validate.validateCustomer(customerId)) {
			throw new InvalidRequestException("Invalid Token");
		}
		validate.validateCustomer(customerId);
		String custId = Integer.toString(customerId);
		cartRedisService.clearCart(custId);
		return "All items are cleared";
	}
}
