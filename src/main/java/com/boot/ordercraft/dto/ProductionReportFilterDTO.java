package com.boot.ordercraft.dto;
 
import java.time.LocalDate;
 
public class ProductionReportFilterDTO {
    private Long categoryId;       // Filter by product category
    private LocalDate startDate;   // Filter by scheduled start date
    private LocalDate endDate;     // Filter by scheduled end date
    private String status;         // PENDING or COMPLETED
 
    public Long getCategoryId() {
        return categoryId;
    }
 
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
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
 
 
 