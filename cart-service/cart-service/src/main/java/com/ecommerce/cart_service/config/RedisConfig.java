package com.ecommerce.cart_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.ecommerce.cart_service.dto.CartItem;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;

@Configuration
public class RedisConfig {

    @SuppressWarnings("removal")
	@Bean
    public RedisTemplate<String, CartItem> cartItemRedisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, CartItem> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        // Key and HashKey as strings
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        // Value and HashValue as JSON (CartItem)
        Jackson2JsonRedisSerializer<CartItem> serializer = new Jackson2JsonRedisSerializer<>(CartItem.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.activateDefaultTyping(
            LaissezFaireSubTypeValidator.instance,
            ObjectMapper.DefaultTyping.NON_FINAL,
            JsonTypeInfo.As.PROPERTY
        );
        
        serializer.setObjectMapper(objectMapper);

        template.setValueSerializer(serializer);
        template.setHashValueSerializer(serializer);

        template.afterPropertiesSet();
        return template;
    }
}
