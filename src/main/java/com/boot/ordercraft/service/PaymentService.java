package com.boot.ordercraft.service;

import com.boot.ordercraft.model.Payment;
import com.boot.ordercraft.model.PurchaseOrder;
import com.boot.ordercraft.model.PurchaseOrderItem;
import com.boot.ordercraft.repository.PaymentRepository;
import com.boot.ordercraft.repository.PurchaseOrderItemsRepository;
import com.boot.ordercraft.repository.PurchaseOrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
public class PaymentService {
 
    @Autowired
    private PaymentRepository paymentRepository;
 
    @Autowired
    private PurchaseOrdersRepository orderRepository;
 
    @Autowired
    private PurchaseOrderItemsRepository itemRepository;
 
    /**
     * Process payment for a given Purchase Order ID.
     * Prevents duplicate payments if already marked as SUCCESS.
     */
    public Payment processPayment(Long orderId) {
        // Check if payment already exists and is successful
        Optional<Payment> existingPayment = paymentRepository.findByOrderId(orderId);
        if (existingPayment.isPresent() && "SUCCESS".equalsIgnoreCase(existingPayment.get().getStatus())) {
            throw new RuntimeException("Payment already completed for this Order ID");
        }
 
        // Validate order existence
        PurchaseOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
 
        // Fetch items and calculate total
        List<PurchaseOrderItem> items = itemRepository.findByPurchaseOrder(order);
        double totalAmount = items.stream()
                .filter(Objects::nonNull)
                .mapToDouble(item -> Optional.ofNullable(item.getPoiCost()).orElse(0.0f))
                .sum();
 
        // Create and save payment
        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setAmount(totalAmount);
        payment.setPaymentDate(new Date());
        payment.setStatus("SUCCESS");
 
        return paymentRepository.save(payment);
    }
 
    /**
     * Get all payment records.
     */
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
}