package com.boot.ordercraft.repository;

import com.boot.ordercraft.model.Payment;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
 
public interface PaymentRepository extends JpaRepository<Payment, Long> {
	
	
	Optional<Payment> findByOrderId(Long orderId); 
}

 