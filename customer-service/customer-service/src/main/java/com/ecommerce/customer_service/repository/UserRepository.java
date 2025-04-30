package com.ecommerce.customer_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.customer_service.entity.User;

public interface UserRepository extends JpaRepository<User,Integer>{

	Optional<User> findByUserName(String userName);
    Optional<User> findById(Long id);

}
