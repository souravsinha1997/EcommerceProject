package com.ecommerce.notification_service.rabbitMQ;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.ecommerce.notification_service.config.RabbitMQConfig;
import com.ecommerce.notification_service.dto.Notification;
import com.ecommerce.notification_service.service.MailService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

import javax.management.NotificationListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class NotificationListner {

    private final MailService mailService;
    private static final Logger log = LoggerFactory.getLogger(NotificationListener.class);
    
    public NotificationListner(MailService mailService) {
        this.mailService = mailService;
    }

    @RabbitListener(queues = RabbitMQConfig.ORDER_NOTIFICATION_QUEUE)
    public void receiveOrderMessage(@Payload Notification notification, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
        try {
            String subject = "Order Notification";
            String message = buildOrderMessage(notification);
            sendNotification(notification, subject, message);
            channel.basicAck(tag, false); // Acknowledge on success
        } catch (Exception e) {
            log.error("Failed to process order notification: {}", e.getMessage());
            try {
                channel.basicNack(tag, false, true); // Reject and requeue
            } catch (Exception ex) {
                log.error("Failed to nack message: {}", ex.getMessage());
            }
        }
    }

    @RabbitListener(queues = RabbitMQConfig.PAYMENT_NOTIFICATION_QUEUE)
    public void receivePaymentMessage(@Payload Notification notification, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
        try {
            String subject = "Payment Notification";
            String message = buildPaymentMessage(notification);
            sendNotification(notification, subject, message);
            channel.basicAck(tag, false); // Acknowledge on success
        } catch (Exception e) {
            log.error("Failed to process payment notification: {}", e.getMessage());
            try {
                channel.basicNack(tag, false, true); // Reject and requeue
            } catch (Exception ex) {
                log.error("Failed to nack message: {}", ex.getMessage());
            }
        }
    }

    private void sendNotification(Notification notification, String subject, String message) {
        mailService.sendSimpleEmail(notification.getUsermail(), subject, message);
        System.out.println("Customer mail " + notification.getUsermail());
        System.out.println(message);
    }

    private String buildOrderMessage(Notification notification) {
        String status = notification.getStatus();
        return switch (status) {
            case "PAID" -> "Successful placed order for Order ID : " + notification.getOrderId() +
                             " with payment amount : " + notification.getAmount();
            case "FAILED" -> "Failed to placed order for Order ID : " + notification.getOrderId() +
                              " with payment amount : " + notification.getAmount();
            case "CANCELLED" -> "Canceled order for Order ID : " + notification.getOrderId() +
                                 " with payment amount : " + notification.getAmount();
            default -> "Unknown status for Order ID : " + notification.getOrderId();
        };
    }

    private String buildPaymentMessage(Notification notification) {
        String status = notification.getStatus();
        return switch (status) {
            case "PAID" -> "Successful completed payment for Payment id : " + notification.getOrderId() +
                            " with payment amount : " + notification.getAmount();
            case "PENDING" -> "Please complete the payment for the payment id : " + notification.getOrderId() +
                               " for payment amount : " + notification.getAmount();
            case "CANCELLED", "REFUNDED" -> status + " payment for payment ID : " + notification.getOrderId() +
                                              " with payment amount : " + notification.getAmount();
            default -> "Unknown status for Payment ID : " + notification.getOrderId();
        };
    }
}

//@Service
//public class NotificationListner {
//
//	private final MailService mailService;
//	
//	public NotificationListner(MailService mailService) {
//		this.mailService = mailService;
//	}
//	
//	@RabbitListener(queues = RabbitMQConfig.ORDER_NOTIFICATION_QUEUE)
//    public void receiveOrderMessage(@Payload Notification notification) {
//		
//        if(notification.getStatus().equals("PAID")) {
//        	mailService.sendSimpleEmail(notification.getUsermail(),"Order Notification","Successful placed order for Order ID : "+notification.getOrderId()+" with payment amount : "+notification.getAmount());
//        	System.out.println("Customer mail "+ notification.getUsermail());
//        	System.out.println("Successful placed order for Order ID : "+notification.getOrderId()+" with payment amount : "+notification.getAmount());
//        }
//        else if(notification.getStatus().equals("FAILED")) {
//        	mailService.sendSimpleEmail(notification.getUsermail(),"Order Notification","Failed to placed order for Order ID : "+notification.getOrderId()+" with payment amount : "+notification.getAmount());
//        	System.out.println("Customer mail "+ notification.getUsermail());
//        	System.out.println("Failed to placed order for Order ID : "+notification.getOrderId()+" with payment amount : "+notification.getAmount());
//        }
//        else if(notification.getStatus().equals("CANCELLED")) {
//        	mailService.sendSimpleEmail(notification.getUsermail(),"Order Notification","Canceled order for Order ID : "+notification.getOrderId()+" with payment amount : "+notification.getAmount());
//        	System.out.println("Customer mail "+ notification.getUsermail());
//        	System.out.println("Canceled order for Order ID : "+notification.getOrderId()+" with payment amount : "+notification.getAmount());
//        }
//    }
//	
//	
//	@RabbitListener(queues = RabbitMQConfig.PAYMENT_NOTIFICATION_QUEUE)
//    public void receivePaymentMessage(@Payload Notification notification) {
//        if(notification.getStatus().equals("PAID")) {
//        	mailService.sendSimpleEmail(notification.getUsermail(),"Payment Notification","Successful completed payment for Payment id : "+notification.getOrderId()+" with payment amount : "+notification.getAmount());
//        	System.out.println("Customer mail "+ notification.getUsermail());
//        	System.out.println("Successful completed payment for Payment id : "+notification.getOrderId()+" with payment amount : "+notification.getAmount());
//        }
//        else if(notification.getStatus().equals("PENDING")) {
//        	mailService.sendSimpleEmail(notification.getUsermail(),"Payment Notification","Please complete the payment fot the payment id : "+notification.getOrderId()+" for payment amount : "+notification.getAmount());
//        	System.out.println("Customer mail "+ notification.getUsermail());
//        	System.out.println("Please complete the payment fot the payment id : "+notification.getOrderId()+" for payment amount : "+notification.getAmount());
//        }
//        else if(notification.getStatus().equals("CANCELLED") || notification.getStatus().equals("REFUNDED")) {
//        	mailService.sendSimpleEmail(notification.getUsermail(),"Payment Notification",notification.getStatus()+" payment for payment ID : "+notification.getOrderId()+" with payment amount : "+notification.getAmount());
//        	System.out.println("Customer mail "+ notification.getUsermail());
//        	System.out.println(notification.getStatus()+" payment for payment ID : "+notification.getOrderId()+" with payment amount : "+notification.getAmount());
//        }
//    }
//}


