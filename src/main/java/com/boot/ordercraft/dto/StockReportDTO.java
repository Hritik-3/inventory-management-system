package com.boot.ordercraft.dto;
 
import java.time.LocalDateTime;
 
public class StockReportDTO {
    private Long productId;
    private String productName;
    private String productDescription;
    private String categoryName;
    private Float unitPrice;
    private Integer quantity;
    private Integer minThreshold;
    private Integer maxThreshold;
    private LocalDateTime lastUpdated;
 
    // Getters and Setters
 
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
 
    public String getProductDescription() {
        return productDescription;
    }
 
    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }
 
    public String getCategoryName() {
        return categoryName;
    }
 
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
 
    public Float getUnitPrice() {
        return unitPrice;
    }
 
    public void setUnitPrice(Float unitPrice) {
        this.unitPrice = unitPrice;
    }
 
    public Integer getQuantity() {
        return quantity;
    }
 
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
 
    public Integer getMinThreshold() {
        return minThreshold;
    }
 
    public void setMinThreshold(Integer minThreshold) {
        this.minThreshold = minThreshold;
    }
 
    public Integer getMaxThreshold() {
        return maxThreshold;
    }
 
    public void setMaxThreshold(Integer maxThreshold) {
        this.maxThreshold = maxThreshold;
    }
 
    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }
 
    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
 