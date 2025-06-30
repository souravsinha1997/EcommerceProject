package com.ecommerce.payment_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.payment_service.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment,Integer>{

	Payment findByOrderId(Integer id);

}
