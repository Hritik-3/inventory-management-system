package com.boot.ordercraft.dto;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class PurchaseOrderCreateRequest {
    private Long supplierId;
    private Date poExpectedDelivery_Date;
    
    private LocalDateTime poOrderDate;
    
    private String poDeliveryStatus;
    
    private String createdBy;
    
    public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	private List<PurchaseOrderItemRequest> items;
    
	public Long getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}
	
	
	
	public String getPoDeliveryStatus() {
		return poDeliveryStatus;
	}
	public void setPoDeliveryStatus(String poDeliveryStatus) {
		this.poDeliveryStatus = poDeliveryStatus;
	}
	public Date getPoExpectedDelivery_Date() {
		return poExpectedDelivery_Date;
	}
	public void setPoExpectedDelivery_Date(Date poExpectedDelivery_Date) {
		this.poExpectedDelivery_Date = poExpectedDelivery_Date;
	}
	public List<PurchaseOrderItemRequest> getItems() {
		return items;
	}
	public void setItems(List<PurchaseOrderItemRequest> items) {
		this.items = items;
	}
	public LocalDateTime getPoOrderDate() {
		return poOrderDate;
	}
	public void setPoOrderDate(LocalDateTime poOrderDate) {
		this.poOrderDate = poOrderDate;
	}
    
	
    
    
}
