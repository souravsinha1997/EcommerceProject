package com.ecommerce.order_service.service.rabbitMQ;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

import com.ecommerce.order_service.config.RabbitMQConfig;
import com.ecommerce.order_service.entity.Notification;
import com.ecommerce.order_service.entity.Order;

@Service
public class OrderPublisher {

    private final AmqpTemplate amqpTemplate;

    public OrderPublisher(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    public void sendOrderToPaymentQueue(Order order) {
        amqpTemplate.convertAndSend(RabbitMQConfig.ORDER_QUEUE, order);
        System.out.println("Published Order ID: " + order.getId());
    }
    
    public void sendOrderToPaymentCancelQueue(Order order) {
        amqpTemplate.convertAndSend(RabbitMQConfig.ORDER_CANCEL_QUEUE, order);
        System.out.println("Canceled Order ID: " + order.getId());
    }
    
    public void sendOrderToNotificationQueue(Notification notification) {
        amqpTemplate.convertAndSend(RabbitMQConfig.ORDER_NOTIFICATION_QUEUE, notification);
        System.out.println("Published Order ID: " + notification.getOrderId());
    }
}
