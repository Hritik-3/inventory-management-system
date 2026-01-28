package com.boot.ordercraft.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import com.boot.ordercraft.model.Payment;
import com.boot.ordercraft.service.PaymentService;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/payments")
public class PaymentController {
 
    @Autowired
    private PaymentService paymentService;
 
    /**
     * Endpoint to process payment for a given order ID.
     * Returns 409 Conflict if payment already exists.
     */
    @PostMapping("/orders/{id}/pay")
    public ResponseEntity<?> payForOrder(@PathVariable Long id) {
        try {
            Payment payment = paymentService.processPayment(id);
            return ResponseEntity.ok(payment);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        }
    }
 
    /**
     * Endpoint to fetch all payments.
     */
//    @GetMapping("/all")
//    public ResponseEntity<List<Payment>> getAllPayments() {
//        return ResponseEntity.ok(paymentService.getAllPayments());
//    }
}
