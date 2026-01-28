package com.boot.ordercraft.dto;

public class PurchaseOrderItemRequest {
    private Long rWId;
    private Integer quantity;
    private Float cost;
	
    
    
	public Long getrWId() {
		return rWId;
	}
	public void setrWId(Long rWId) {
		this.rWId = rWId;
	}
	
	
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Float getCost() {
		return cost;
	}
	public void setCost(Float cost) {
		this.cost = cost;
	}
    
    
    
}
