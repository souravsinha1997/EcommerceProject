package com.ecommerce.order_service.service;

import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

import com.ecommerce.order_service.entity.dto.OrderResponse;
import com.ecommerce.order_service.entity.dto.PaymentResponse;
import com.rabbitmq.client.Channel;

public interface OrderService {
	String placeOrder(int customerId);
	OrderResponse trackOrder(int id);
	//void receiveOrderMessage(@Payload PaymentResponse paymentResponse, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag);
	String cancelOrder(int orderId);
}
