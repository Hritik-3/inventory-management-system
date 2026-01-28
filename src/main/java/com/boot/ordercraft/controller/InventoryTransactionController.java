package com.boot.ordercraft.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.boot.ordercraft.model.InventoryTransaction;
import com.boot.ordercraft.service.InventoryTransactionService;

@RestController
@RequestMapping("/inventory")
public class InventoryTransactionController {

    private final InventoryTransactionService inventoryTransactionService;

    public InventoryTransactionController(InventoryTransactionService inventoryTransactionService) {
        this.inventoryTransactionService = inventoryTransactionService;
    }

    @PostMapping("/transaction")
    public ResponseEntity<InventoryTransaction> createTransaction(@RequestBody TransactionRequest request) {
        InventoryTransaction tx = inventoryTransactionService.createTransaction(
                request.getProductId(),
                request.getPerformedBy(),
                request.getTransactionType(),
                request.getQuantity()
        );
        return ResponseEntity.ok(tx);
    }

    // DTO for the request body
    public static class TransactionRequest {
        private Long productId;
        private String performedBy;
        private String transactionType;
        private Integer quantity;

        public Long getProductId() {
            return productId;
        }
        public void setProductId(Long productId) {
            this.productId = productId;
        }
        public String getPerformedBy() {
            return performedBy;
        }
        public void setPerformedBy(String performedBy) {
            this.performedBy = performedBy;
        }
        public String getTransactionType() {
            return transactionType;
        }
        public void setTransactionType(String transactionType) {
            this.transactionType = transactionType;
        }
        public Integer getQuantity() {
            return quantity;
        }
        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }
    }
}
