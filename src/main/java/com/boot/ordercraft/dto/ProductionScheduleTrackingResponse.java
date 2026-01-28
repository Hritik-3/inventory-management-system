package com.boot.ordercraft.dto;
 
import java.time.LocalDate;
import java.util.Date;
 
public class ProductionScheduleTrackingResponse {
    private Long scheduleId;
    private Long productId;
    private String productName;
    private Integer quantity;
    private Date startDate;
    private Date endDate;
    private String status;
    
    private String actions;
 
    public String getActions() {
        return actions;
    }
 
    public void setActions(String actions) {
        this.actions = actions;
    }
 
     
    
    public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public Long getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(Long scheduleId) {
		this.scheduleId = scheduleId;
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
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date date) {
		this.startDate = date;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date date) {
		this.endDate = date;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
//	public String getPerformedBy() {
//		return performedBy;
//	}
//	public void setPerformedBy(String performedBy) {
//		this.performedBy = performedBy;
//	}
//	private String performedBy;
//
//	
    // getters and setters
}
 
 
 
 