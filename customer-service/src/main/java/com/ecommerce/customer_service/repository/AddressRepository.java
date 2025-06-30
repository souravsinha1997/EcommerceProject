package com.ecommerce.customer_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.customer_service.entity.Address;

public interface AddressRepository extends JpaRepository<Address,Integer>{

}
