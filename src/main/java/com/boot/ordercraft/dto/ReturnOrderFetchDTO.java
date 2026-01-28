package com.boot.ordercraft.dto;

import java.util.Date;
import java.util.List;

public class ReturnOrderFetchDTO {
    private Long roId;
    private Date roReturnDate;
    private String roReturnReason;
    private String roStatus;
    private Long purchaseOrderId;
    private String returnedByUserId; // You can adjust based on User entity
    private List<ReturnOrderItemFetchDTO> items; 
	public Long getRoId() {
		return roId;
	}
	public void setRoId(Long roId) {
		this.roId = roId;
	}
	public Date getRoReturnDate() {
		return roReturnDate;
	}
	public void setRoReturnDate(Date roReturnDate) {
		this.roReturnDate = roReturnDate;
	}
	public String getRoReturnReason() {
		return roReturnReason;
	}
	public void setRoReturnReason(String roReturnReason) {
		this.roReturnReason = roReturnReason;
	}
	public String getRoStatus() {
		return roStatus;
	}
	public void setRoStatus(String roStatus) {
		this.roStatus = roStatus;
	}
	public Long getPurchaseOrderId() {
		return purchaseOrderId;
	}
	public void setPurchaseOrderId(Long purchaseOrderId) {
		this.purchaseOrderId = purchaseOrderId;
	}
	public String getReturnedByUserId() {
		return returnedByUserId;
	}
	public void setReturnedByUserId(String returnedByUserId) {
		this.returnedByUserId = returnedByUserId;
	}
	public List<ReturnOrderItemFetchDTO> getItems() {
		return items;
	}
	public void setItems(List<ReturnOrderItemFetchDTO> items) {
		this.items = items;
	}
	

    // Getters and Setters
    
    
}


