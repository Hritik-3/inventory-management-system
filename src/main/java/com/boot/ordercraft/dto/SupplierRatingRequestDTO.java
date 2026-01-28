package com.boot.ordercraft.dto;
 
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
 
public class SupplierRatingRequestDTO {
 
    @NotNull(message = "Supplier ID is required")
    private Long supplierId;
 
    @NotNull(message = "Rating value is required")
    @DecimalMin(value = "0.0", message = "Rating must be at least 0")
    @DecimalMax(value = "5.0", message = "Rating cannot exceed 5")
    private Double ratingValue;
 
    private String comments;
 
    // Getters and setters
    public Long getSupplierId() { return supplierId; }
    public void setSupplierId(Long supplierId) { this.supplierId = supplierId; }
 
    public Double getRatingValue() { return ratingValue; }
    public void setRatingValue(Double ratingValue) { this.ratingValue = ratingValue; }
 
    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }
}
 
 