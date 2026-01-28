package com.boot.ordercraft.dto;
 
import java.time.LocalDate;
 
public class InventoryReportFilterDTO {
    private Long categoryId;        // Optional category filter
    private LocalDate startDate;    // Optional start date
    private LocalDate endDate;      // Optional end date
    private String status;          // Optional status filter (PENDING, COMPLETED)
    private String transactionType;
 
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
 
	public String getTransactionType() {
		return transactionType;
	}
 
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
 
}
 
 
 
 