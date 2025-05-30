package com.ecommerce.payment_service.service;

import java.time.LocalDateTime;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.payment_service.client.CustomerClient;
import com.ecommerce.payment_service.client.dto.UserResponse;
import com.ecommerce.payment_service.config.RabbitMQConfig;
import com.ecommerce.payment_service.dto.Notification;
import com.ecommerce.payment_service.dto.Order;
import com.ecommerce.payment_service.entity.Payment;
import com.ecommerce.payment_service.exception.CustomerNotFoundException;
import com.ecommerce.payment_service.exception.InvalidPaymentIdException;
import com.ecommerce.payment_service.exception.InvalidTokenException;
import com.ecommerce.payment_service.exception.PaymentException;
import com.ecommerce.payment_service.repository.PaymentRepository;
import com.ecommerce.payment_service.security.ValidateRequest;
import com.ecommerce.payment_service.service.rabbitMQ.PaymentPublisher;

@Service
public class PaymentServiceImpl implements PaymentService{

	private final PaymentRepository paymentRepo;
	private final ValidateRequest validate;
	private final PaymentPublisher paymentPublisher;
	private final CustomerClient customerClient;
	
	public PaymentServiceImpl(CustomerClient customerClient,PaymentRepository paymentRepo, ValidateRequest validate,PaymentPublisher paymentPublisher) {
		this.paymentRepo = paymentRepo;
		this.validate = validate;
		this.paymentPublisher = paymentPublisher;
		this.customerClient = customerClient;
	}
	
	@Transactional
	public String payTheAmount(int id) {
		
		Payment payment = paymentRepo.findById(id).orElseThrow(()-> new InvalidPaymentIdException("Invalid payment id"));	
		if(!validate.validateCustomer(payment.getUserId())) {
			throw new InvalidTokenException("Invalid Token");
		}
		if(payment.getStatus().equals("PENDING"))
			payment.setStatus("PAID");

		payment.setPaidAt(LocalDateTime.now());
		Payment savedPayment = paymentRepo.save(payment);
		paymentPublisher.sendPaymentUpdate(savedPayment);
		
		
		ResponseEntity<UserResponse> customer = customerClient.getUser(payment.getUserId());
		Notification notification = new Notification();
		notification.setAmount(payment.getAmount());
		notification.setOrderId(payment.getId());
		notification.setStatus(payment.getStatus());
		notification.setUsermail(customer.getBody().getEmail());
		notification.setUserName(customer.getBody().getUserName());
		
		paymentPublisher.sendNotificationUpdate(notification);
		
		return "Payment successful for the Payment ID : "+id;
	}
	
	@Transactional
	@RabbitListener(queues = RabbitMQConfig.ORDER_QUEUE)
    public void receiveOrderMessage(@Payload Order order) {
		 try {
		        Payment payment = new Payment();
		        payment.setOrderId(order.getId());
		        payment.setCreatedAt(order.getOrderDate());
		        payment.setUserId(order.getCustomerId());
		        payment.setAmount(order.getTotalPrice());
		        payment.setStatus("PENDING");

		        // Validate user before saving payment
		        ResponseEntity<UserResponse> customer = customerClient.getUser(order.getCustomerId());
		        if (customer == null || customer.getBody() == null) {
		            throw new CustomerNotFoundException("Customer not found");
		        }

		        // Save payment only after successful customer fetch
		        Payment savedPayment = paymentRepo.save(payment);

		        Notification notification = new Notification();
		        notification.setAmount(savedPayment.getAmount());
		        notification.setOrderId(savedPayment.getId());
		        notification.setStatus(savedPayment.getStatus());
		        notification.setUsermail(customer.getBody().getEmail());
		        notification.setUserName(customer.getBody().getUserName());

		        paymentPublisher.sendNotificationUpdate(notification);

		        System.out.println("Payment pending for id : " + savedPayment.getId());

		    } catch (Exception e) {
		        // Log and let Spring roll back the transaction
		        System.err.println("Error processing order: " + e.getMessage());
		        throw new PaymentException("Internal server error"); // rethrow to trigger transaction rollback
		    }
    }
	
	@Transactional
	@RabbitListener(queues = RabbitMQConfig.ORDER_CANCEL_QUEUE)
    public void cancelOrderMessage(@Payload Order order) {
		try {
			Payment payment = paymentRepo.findByOrderId(order.getId());
		
			if(payment.getStatus().equals("PAID"))
				payment.setStatus("REFUNDED");
			else
				payment.setStatus("CANCELLED");
     
			ResponseEntity<UserResponse> customer = customerClient.getUser(payment.getUserId());
			Notification notification = new Notification();
			notification.setAmount(payment.getAmount());
			notification.setOrderId(payment.getId());
			notification.setStatus(payment.getStatus());
			notification.setUsermail(customer.getBody().getEmail());
			notification.setUserName(customer.getBody().getUserName());
		
			paymentPublisher.sendNotificationUpdate(notification);
		
			Payment savedPayment = paymentRepo.save(payment);
			System.out.println( "Payment refunded for id : "+savedPayment.getId());
		}
		catch (Exception e) {
	        // Log and let Spring roll back the transaction
	        System.err.println("Error processing order: " + e.getMessage());
	        throw new PaymentException("Internal server error"); // rethrow to trigger transaction rollback
	    }
	}

}
