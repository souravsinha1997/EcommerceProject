package com.ecommerce.payment_service.config;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String ORDER_QUEUE = "order.queue";
    public static final String PAYMENT_QUEUE = "payment.queue";
    public static final String ORDER_CANCEL_QUEUE = "order.cancel.queue";
    public static final String PAYMENT_NOTIFICATION_QUEUE = "payment.notification.queue";
    
    @Bean
    public Queue orderQueue() {
        return new Queue(ORDER_QUEUE, true);
    }
    
    @Bean
    public Queue paymentQueue() {
        return new Queue(PAYMENT_QUEUE, true);
    }
    
    @Bean
    public Queue paymentCancelQueue() {
        return new Queue(ORDER_CANCEL_QUEUE, true);
    }
    
    @Bean
    public Queue paymentNotificationQueue() {
        return new Queue(PAYMENT_NOTIFICATION_QUEUE, true);
    }
    
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter(); // Ensures JSON serialization
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
