package com.ecommerce.cart_service.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.ecommerce.cart_service.dto.CartItem;

@Service
public class CartRedisService {

    @Autowired
    private RedisTemplate<String, CartItem> cartItemRedisTemplate;

    private static final String CART_KEY_PREFIX = "cart:";

    public void addToCart(String customerId, CartItem item) {
        String key = CART_KEY_PREFIX + customerId;

        cartItemRedisTemplate.opsForHash().put(key, String.valueOf(item.getProductId()), item);
    }

    public List<CartItem> getCart(String customerId) {
        String key = CART_KEY_PREFIX + customerId;

        Map<Object, Object> entries = cartItemRedisTemplate.opsForHash().entries(key);
        return entries.values().stream()
                .map(obj -> (CartItem) obj)
                .collect(Collectors.toList());
    }
    
    public CartItem getCartItemByProductId(String customerId, int productId) {
        String key = CART_KEY_PREFIX + customerId;

        return (CartItem) cartItemRedisTemplate.opsForHash().get(key, String.valueOf(productId));
    }

    public void updateCartItem(String customerId, int productId, int newQuantity) {
        String key = CART_KEY_PREFIX + customerId;
        
        CartItem existingItem = (CartItem) cartItemRedisTemplate.opsForHash().get(key, String.valueOf(productId));

        if (existingItem != null) {
            existingItem.setQuantity(newQuantity);
            cartItemRedisTemplate.opsForHash().put(key, String.valueOf(productId), existingItem);
        }
    }

    public void removeFromCart(String customerId, int productId) {
        String key = CART_KEY_PREFIX + customerId;

        cartItemRedisTemplate.opsForHash().delete(key, String.valueOf(productId));
    }

    public void clearCart(String customerId) {
        String key = CART_KEY_PREFIX + customerId;

        cartItemRedisTemplate.delete(key);
    }
}
