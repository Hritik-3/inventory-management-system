package com.boot.ordercraft.dto;
 
import java.time.LocalDateTime;
 
public class InventoryTransactionReportDTO {
    private Long transactionId;
    private Long productId;
    private String productName;
    private Integer quantity;
    private String transactionType;  // IN or OUT
    private LocalDateTime transactionDate;
 
    // Getters and Setters
 
    public Long getTransactionId() {
        return transactionId;
    }
 
    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }
 
    public Long getProductId() {
        return productId;
    }
 
    public void setProductId(Long productId) {
        this.productId = productId;
    }
 
    public String getProductName() {
        return productName;
    }
 
    public void setProductName(String productName) {
        this.productName = productName;
    }
 
    public Integer getQuantity() {
        return quantity;
    }
 
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
 
    public String getTransactionType() {
        return transactionType;
    }
 
    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }
 
    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }
 
    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }
}
 