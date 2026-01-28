package com.boot.ordercraft.dto;
 
import java.time.LocalDate;
 
public class ProductionReportDTO {
    private Long productionId;
    private String productName;
    private String categoryName;
    private int quantityPlanned;
    private int quantityCompleted;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;   // PENDING or COMPLETED
 
    public Long getProductionId() {
        return productionId;
    }
 
    public void setProductionId(Long productionId) {
        this.productionId = productionId;
    }
 
    public String getProductName() {
        return productName;
    }
 
    public void setProductName(String productName) {
        this.productName = productName;
    }
 
    public String getCategoryName() {
        return categoryName;
    }
 
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
 
    public int getQuantityPlanned() {
        return quantityPlanned;
    }
 
    public void setQuantityPlanned(int quantityPlanned) {
        this.quantityPlanned = quantityPlanned;
    }
 
    public int getQuantityCompleted() {
        return quantityCompleted;
    }
 
    public void setQuantityCompleted(int quantityCompleted) {
        this.quantityCompleted = quantityCompleted;
    }
 
    public LocalDate getStartDate() {
        return startDate;
    }
 
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
 
    public LocalDate getEndDate() {
        return endDate;
    }
 
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
 
    public String getStatus() {
        return status;
    }
 
    public void setStatus(String status) {
        this.status = status;
    }
}
 
 
 