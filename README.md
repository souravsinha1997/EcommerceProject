EcommerceProject

Overview This project is a comprehensive e-commerce platform designed to handle various aspects of online shopping, including user management, product catalog, cart operations, order processing, payment handling, and notifications. It follows a microservices architecture to ensure scalability and maintainability.

Architecture

The application is structured into several microservices:

cart-service: Manages shopping cart functionalities.

config-server: Centralized configuration management for all services.

customer-service: Handles customer registration, authentication, and profile management.

eureka-service: Service discovery using Netflix Eureka.

gateway-service: API Gateway that routes requests to appropriate services.

notification-service: Sends notifications to users (e.g., order confirmations).

order-service: Processes orders and manages order history.

payment-service: Handles payment transactions.

product_service: Manages product listings and inventory.

API Documentation

API documentation for the services is available in the following directories:

API_Documentation_CustomerService_CartService

API_Documentation_OrderService_PaymentService

API_Documentation_ProductService

These documents provide detailed information about the available endpoints, request/response formats, and usage examples.

Database Queries

The DatabaseQueries directory contains SQL scripts and queries related to the database operations of the application.

Set up the configuration server:

Navigate to the config-server directory and start the configuration server.

Start the Eureka service:

Navigate to the eureka-service directory and run the Eureka server for service discovery.

Start the gateway service:

Navigate to the gateway-service directory and run the API gateway.

Start individual services:

For each service (cart-service, customer-service, notification-service, order-service, payment-service, product_service), navigate to its directory and run the service.

Ensure that each service is configured correctly and connected to the appropriate databases.

Technologies Used

Spring Boot: For building the microservices.

Spring Cloud: For implementing service discovery and configuration management.

Netflix Eureka: For service registration and discovery.

Spring Cloud Gateway: As the API Gateway.

MySQL: As the relational database for storing data.

RabbitMQ: For handling asynchronous communication between services.

Swagger/OpenAPI: For API documentation.