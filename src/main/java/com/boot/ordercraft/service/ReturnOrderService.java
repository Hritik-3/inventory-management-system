package com.boot.ordercraft.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.boot.ordercraft.dto.ApiResponseDTO;
import com.boot.ordercraft.dto.ReturnOrderItemDTO;
import com.boot.ordercraft.dto.ReturnOrderRequestDTO;
import com.boot.ordercraft.model.*;
import com.boot.ordercraft.repository.*;

import com.boot.ordercraft.service.MailService.MailService;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class ReturnOrderService {

    @Autowired private ReturnOrdersRepository returnOrderRepository;
    @Autowired private ReturnOrderItemsRepository returnOrderItemRepository;
    @Autowired private PurchaseOrdersRepository purchaseOrderRepository;
    @Autowired private ProductsRepository productRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private MailService emailService;
    @Autowired private PurchaseOrderItemsRepository purchaseOrderItemRepository;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Transactional
    public ResponseEntity<ApiResponseDTO> createReturnOrder(ReturnOrderRequestDTO dto) {
        String userId = dto.getReturnedByUserId();
        if (userId == null) {
            throw new IllegalArgumentException("ReturnedByUserId is null.");
        }
        System.out.println("Looking for User ID: " + userId);

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        if (dto.getPurchaseOrderId() == null) {
            throw new IllegalArgumentException("purchaseOrderId is null.");
        }
        System.out.println("Looking for Purchase Order ID: " + dto.getPurchaseOrderId());

        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(dto.getPurchaseOrderId())
                .orElseThrow(() -> new RuntimeException("Purchase Order not found with ID: " + dto.getPurchaseOrderId()));
        
        // 🔹 Check if order is delivered
        if (!"DELIVERED".equalsIgnoreCase(purchaseOrder.getPoDeliveryStatus())) {
            return ResponseEntity.ok(new ApiResponseDTO(
                    "Return cannot be created. Purchase Order is not yet delivered.",
                    null));
        }

        ReturnOrder returnOrder = new ReturnOrder();
        returnOrder.setReturnedBy(user);
        returnOrder.setRoStatus("Pending"); // 🔹 Always start as Pending
        returnOrder.setRoReturnReason(dto.getReturnReason());
        returnOrder.setPurchaseOrder(purchaseOrder);

        List<ReturnOrderItem> returnItems = new ArrayList<>();
        Customer customer = null;

        for (ReturnOrderItemDTO itemDto : dto.getItems()) {
            System.out.println("Processing item with POI ID: " + itemDto.getPurchaseOrderItemId());

            if (itemDto.getPurchaseOrderItemId() == null) {
                throw new IllegalArgumentException("purchaseOrderItemId is null for one of the items.");
            }

            PurchaseOrderItem poi = purchaseOrderItemRepository.findById(itemDto.getPurchaseOrderItemId())
                    .orElseThrow(() -> new RuntimeException("PO item not found with ID: " + itemDto.getPurchaseOrderItemId()));

            Integer alreadyReturnedQty = Optional.ofNullable(
                    returnOrderItemRepository.sumReturnedQuantityByPurchaseOrderItemId(poi.getPoiId())
            ).orElse(0);

            int remainingQty = poi.getPoiQuantity() - alreadyReturnedQty;
            int returnQty = itemDto.getQuantity();

            if (returnQty > remainingQty) {
                return ResponseEntity.ok(new ApiResponseDTO(
                        "Requested return quantity exceeds the remaining available quantity.",
                        null));
            }

            if (remainingQty <= 0) {
                return ResponseEntity.ok(new ApiResponseDTO(
                        "This order item has already been fully returned.",
                        null));
            }

            if (remainingQty == returnQty) {
                poi.setReturned(true);
                purchaseOrderItemRepository.save(poi);
            }

            ReturnOrderItem roi = new ReturnOrderItem();
            roi.setReturnOrder(returnOrder);
            roi.setProduct(poi.getProduct());
            roi.setPurchaseOrderItem(poi);
            roi.setReturnQuantity(returnQty);
            roi.setConditionNote(itemDto.getConditionNote());
            returnItems.add(roi);

            if (customer == null) {
                customer = purchaseOrder.getCustomer();
            }
        }

        returnOrder.setItems(returnItems);
        returnOrder.setPurchaseOrder(purchaseOrder);

        ReturnOrder savedOrder = returnOrderRepository.save(returnOrder);
        returnOrderItemRepository.saveAll(returnItems);

        // 🔹 Schedule status update to COMPLETE after 10 seconds
        scheduler.schedule(() -> {
            try {
                Optional<ReturnOrder> optionalOrder = returnOrderRepository.findById(savedOrder.getRoId());
                if (optionalOrder.isPresent()) {
                    ReturnOrder orderToUpdate = optionalOrder.get();
                    orderToUpdate.setRoStatus("Complete");
                    returnOrderRepository.save(orderToUpdate);
                    System.out.println("✅ Return Order #" + savedOrder.getRoId() + " updated to COMPLETE");
                }
            } catch (Exception e) {
                System.err.println("Failed to update order status: " + e.getMessage());
            }
        }, 30, TimeUnit.SECONDS);

        if (customer != null && customer.getEmail() != null) {
            try {
                emailService.sendEmail(
                        customer.getEmail(),
                        "Return Confirmation - PO " + purchaseOrder.getPoId(),
                        String.format("""
                                Hi %s,

                                Your return for order #%d was successfully submitted.

                                Reason: %s

                                Date: %s

                                Thank you,

                                OrderCraft
                                """,
                                customer.getName(),
                                purchaseOrder.getPoId(),
                                dto.getReturnReason(),
                                new Date()
                        )
                );
            } catch (Exception e) {
                System.err.println("Email sending failed: " + e.getMessage());
            }
        }

        String responseMessage = String.format("Return order submitted successfully for customer: %s",
                customer != null ? customer.getName() : "N/A");

        return ResponseEntity.ok(new ApiResponseDTO(responseMessage, savedOrder.getRoId()));
    }
}
