package com.ecommerce.payment_service.service.rabbitMQ;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.payment_service.config.RabbitMQConfig;
import com.ecommerce.payment_service.dto.Notification;
import com.ecommerce.payment_service.entity.Payment;

@Service
public class PaymentPublisher {

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void sendPaymentUpdate(Payment payment) {
        amqpTemplate.convertAndSend(RabbitMQConfig.PAYMENT_QUEUE, payment);
    }
    
    public void sendNotificationUpdate(Notification notification) {
        amqpTemplate.convertAndSend(RabbitMQConfig.PAYMENT_NOTIFICATION_QUEUE, notification);
    }
}