package com.boot.ordercraft.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class PurchaseOrderUpdateRequest {
    private LocalDate newExpectedDeliveryDate;
    private String newDeliveryStatus;
    private List<ItemUpdateRequest> items;
    
    

    // getters and setters

    public LocalDate getNewExpectedDeliveryDate() {
		return newExpectedDeliveryDate;
	}



	public void setNewExpectedDeliveryDate(LocalDate newExpectedDeliveryDate) {
		this.newExpectedDeliveryDate = newExpectedDeliveryDate;
	}



	public String getNewDeliveryStatus() {
		return newDeliveryStatus;
	}



	public void setNewDeliveryStatus(String newDeliveryStatus) {
		this.newDeliveryStatus = newDeliveryStatus;
	}



	public List<ItemUpdateRequest> getItems() {
		return items;
	}



	public void setItems(List<ItemUpdateRequest> items) {
		this.items = items;
	}



	public static class ItemUpdateRequest {
        private Long itemId;
        private Integer newQuantity;
        private Float newCost;
        // getters and setters
		public Long getItemId() {
			return itemId;
		}
		public void setItemId(Long itemId) {
			this.itemId = itemId;
		}
		public Integer getNewQuantity() {
			return newQuantity;
		}
		public void setNewQuantity(Integer newQuantity) {
			this.newQuantity = newQuantity;
		}
		public Float getNewCost() {
			return newCost;
		}
		public void setNewCost(Float newCost) {
			this.newCost = newCost;
		}
        
        
        
    }
}
