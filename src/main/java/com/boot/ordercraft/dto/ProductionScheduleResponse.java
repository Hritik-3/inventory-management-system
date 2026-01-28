package com.boot.ordercraft.dto;

import java.util.Date;

public class ProductionScheduleResponse {
    private Long scheduleId;
    private String productName;
    private Integer quantity;
    private String status;        // final status
    private String currentStatus; // dynamic status during execution
    private Long productId;
    private Date startDate;
    private Date endDate;
    private String productionLineName;
    private String message;

    public ProductionScheduleResponse() {}

    public ProductionScheduleResponse(Long scheduleId, String productName, Integer quantity, String status, String productionLineName) {
        this.scheduleId = scheduleId;
        this.productName = productName;
        this.quantity = quantity;
        this.status = status;
        this.currentStatus = status; // initially same
        this.productionLineName = productionLineName;
    }

    // Getters & setters
    public Long getScheduleId() { return scheduleId; }
    public void setScheduleId(Long scheduleId) { this.scheduleId = scheduleId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCurrentStatus() { return currentStatus; }
    public void setCurrentStatus(String currentStatus) { this.currentStatus = currentStatus; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }

    public String getProductionLineName() { return productionLineName; }
    public void setProductionLineName(String productionLineName) { this.productionLineName = productionLineName; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
