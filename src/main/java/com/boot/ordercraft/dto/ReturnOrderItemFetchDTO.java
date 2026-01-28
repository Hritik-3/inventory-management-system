package com.boot.ordercraft.dto;

import java.util.Date;
import java.util.List;


public class ReturnOrderItemFetchDTO {
    private Long roiId;
    private Integer returnQuantity;
    private String conditionNote;
    private Long productId;
    private String productName; // optional
    private Long purchaseOrderItemId;
	public Long getRoiId() {
		return roiId;
	}
	public void setRoiId(Long roiId) {
		this.roiId = roiId;
	}
	public Integer getReturnQuantity() {
		return returnQuantity;
	}
	public void setReturnQuantity(Integer returnQuantity) {
		this.returnQuantity = returnQuantity;
	}
	public String getConditionNote() {
		return conditionNote;
	}
	public void setConditionNote(String conditionNote) {
		this.conditionNote = conditionNote;
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
	public Long getPurchaseOrderItemId() {
		return purchaseOrderItemId;
	}
	public void setPurchaseOrderItemId(Long purchaseOrderItemId) {
		this.purchaseOrderItemId = purchaseOrderItemId;
	}
    
    

    // Getters and Setters
}
