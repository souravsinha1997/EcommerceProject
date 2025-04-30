package com.ecommerce.order_service.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.order_service.client.CartClient;
import com.ecommerce.order_service.client.CustomerClient;
import com.ecommerce.order_service.client.ProductClient;
import com.ecommerce.order_service.client.dto.CartItem;
import com.ecommerce.order_service.client.dto.CartResponse;
import com.ecommerce.order_service.client.dto.ProductRequest;
import com.ecommerce.order_service.client.dto.ProductResponse;
import com.ecommerce.order_service.client.dto.UserResponse;
import com.ecommerce.order_service.config.RabbitMQConfig;
import com.ecommerce.order_service.entity.Notification;
import com.ecommerce.order_service.entity.Order;
import com.ecommerce.order_service.entity.OrderItem;
import com.ecommerce.order_service.entity.dto.OrderItemResponse;
import com.ecommerce.order_service.entity.dto.OrderResponse;
import com.ecommerce.order_service.entity.dto.PaymentResponse;
import com.ecommerce.order_service.repository.OrderRepository;
import com.ecommerce.order_service.service.rabbitMQ.OrderPublisher;

@Service
public class OrderService {

	private final OrderRepository orderRepo;
	private final ProductClient productClient;
	private final CustomerClient customerClient;
	private final CartClient cartClient;
	private final OrderPublisher orderPublisher;
	private final ValidateRequest validate;
	
	public OrderService(OrderRepository orderRepo,ProductClient productClient,
			CustomerClient customerClient,CartClient cartClient,
			OrderPublisher orderPublisher,ValidateRequest validate) {
		this.orderRepo = orderRepo;
		this.productClient = productClient;
		this.cartClient = cartClient;
		this.customerClient = customerClient;
		this.orderPublisher = orderPublisher;
		this.validate = validate;
	}
	
	@Transactional
	public String placeOrder(int customerId) {
		
		if(!validate.validateCustomer(customerId)) {
			throw new RuntimeException("Invalid token");
		}
		ResponseEntity<CartResponse> cart = cartClient.getAllItems(customerId);
		
		if(cart.getBody().getItems().isEmpty()) {
			throw new RuntimeException("Cart is empty, please add items in your cart");
		}
		
		//ResponseEntity<UserResponse> user = customerClient.getUser(customerId);
		
		
		double totalAmount = 0;
		for(CartItem item : cart.getBody().getItems()) {
			totalAmount += item.getPrice()*item.getQuantity();
		}
		
		Order order = new Order();
		order.setCustomerId(customerId); 
		order.setOrderDate(LocalDateTime.now());
		order.setStatus("PENDING");
		order.setTotalPrice(BigDecimal.valueOf(totalAmount));
		
		List<OrderItem> orderItems = new ArrayList<>();
		for(CartItem item : cart.getBody().getItems()) {
			//ResponseEntity<ProductResponse> product = productClient.getProductById(item.getProductId());
			OrderItem orderItem = new OrderItem();
			orderItem.setProductId(item.getProductId());
			orderItem.setQuantity(item.getQuantity());
			orderItem.setUnitPrice(BigDecimal.valueOf(item.getPrice()));
			orderItem.setOrder(order);
			orderItems.add(orderItem);
		}
		
		order.setOrderItems(orderItems);
		Order savedOrder = orderRepo.save(order);
		cartClient.clearCart(customerId);
		//use RabbitMQ to share order details to payment service
		orderPublisher.sendOrderToPaymentQueue(savedOrder);
		
		return "Please complete the payment for Order ID "+savedOrder.getId();
	}
	
	@Transactional
	public OrderResponse trackOrder(int id) {
		Order order = orderRepo.findById(id).orElseThrow(()-> new RuntimeException("Invalid order id"));
		if(!validate.validateCustomer(order.getCustomerId())) {
			throw new RuntimeException("Invalid token");
		}
		//get customer details using customer client
		ResponseEntity<UserResponse> customer = customerClient.getUser(order.getCustomerId());	
		String customerName = customer.getBody().getUserName();
		OrderResponse response = new OrderResponse();
		
		response.setCustomerName(customerName);
		response.setOrderDate(order.getOrderDate());
		response.setTotalPrice(order.getTotalPrice());
		response.setOrderId(id);
		response.setStatus(order.getStatus());
		
		List<OrderItemResponse> itemResponse = new ArrayList<>();
		List<OrderItem> items = order.getOrderItems();
		
		for(OrderItem item : items) {
			ResponseEntity<ProductResponse> product = productClient.getProductById(item.getProductId());
			OrderItemResponse orderItemResponse = new OrderItemResponse();
			orderItemResponse.setPrice(item.getUnitPrice());
			orderItemResponse.setProductName(product.getBody().getName());
			orderItemResponse.setQuantity(item.getQuantity());
			
			itemResponse.add(orderItemResponse);
		}
		
		response.setItems(itemResponse);
		return response;
	}
	
	@RabbitListener(queues = RabbitMQConfig.PAYMENT_QUEUE)
	@Transactional
    public void receiveOrderMessage(@Payload PaymentResponse paymentResponse) {
        int customerId = paymentResponse.getUserId();
        int order_id = paymentResponse.getOrderId();
        
        Order savedOrder = orderRepo.findById(order_id).orElseThrow(()-> new RuntimeException("Invalid order no"));
        if(paymentResponse.getStatus().equals("PAID")) {
        	
        	if(savedOrder.getStatus().equals("SUCCESS")) return;
        	if(savedOrder.getStatus().equals("PENDING") && savedOrder.getCustomerId()==customerId) {
        		savedOrder.setStatus("PAID");
        	     List<OrderItem> orderItems = savedOrder.getOrderItems();  
        	     for(OrderItem item : orderItems) {
        	    	 ResponseEntity<ProductResponse> product = productClient.getProductById(item.getProductId());
        	    	 int quantity = product.getBody().getQuantity() - item.getQuantity();
        	    	 ProductRequest productRequest = new ProductRequest();
        	    	 productRequest.setQuantity(quantity);
        	    	 productClient.updateStock(productRequest,item.getProductId());
        	     }
        	}
        	else {
        		savedOrder.setStatus("FAILED");
        	}
        	orderRepo.save(savedOrder);
        }    
        
        ResponseEntity<UserResponse> customer = customerClient.getUser(savedOrder.getCustomerId());	
        
        Notification notification = new Notification();
        notification.setOrderId(savedOrder.getId());
        notification.setStatus(savedOrder.getStatus());
        notification.setAmount(savedOrder.getTotalPrice());
        notification.setUserName(customer.getBody().getUserName());
        notification.setUsermail(customer.getBody().getEmail());
        
        orderPublisher.sendOrderToNotificationQueue(notification);
        
     	System.out.println("Order placed successfully!");
    }
	
	@Transactional
	public String cancelOrder(int orderId) {
		Order order = orderRepo.findById(orderId).orElseThrow(()-> new RuntimeException("Invalid Order Number"));
		
		order.setStatus("CANCELLED");
		
		Order canceledOrder = orderRepo.save(order);
		
		orderPublisher.sendOrderToPaymentCancelQueue(canceledOrder);
		
		ResponseEntity<UserResponse> customer = customerClient.getUser(canceledOrder.getCustomerId());	
		Notification notification = new Notification();
		notification.setAmount(canceledOrder.getTotalPrice());
		notification.setOrderId(canceledOrder.getId());
		notification.setStatus(canceledOrder.getStatus());
		notification.setUsermail(customer.getBody().getEmail());
		notification.setUserName(customer.getBody().getUserName());
		
		orderPublisher.sendOrderToNotificationQueue(notification);
		
		
		return "Order canceled for the order id : "+canceledOrder.getId();
	}
}
