package com.boot.ordercraft.dto;
 
public class ReturnOrderItemDTO {
	private Long purchaseOrderItemId;
    private Long productId;
    private Integer quantity;
    private String conditionNote;
 
    // Getters and Setters
    public Long getProductId() {
        return productId;
    }
    public void setProductId(Long productId) {
        this.productId = productId;
    }
 
    public Long getPurchaseOrderItemId() {
		return purchaseOrderItemId;
	}
	public void setPurchaseOrderItemId(Long purchaseOrderItemId) {
		this.purchaseOrderItemId = purchaseOrderItemId;
	}
	public Integer getQuantity() {
        return quantity;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
 
    public String getConditionNote() {
        return conditionNote;
    }
    public void setConditionNote(String conditionNote) {
        this.conditionNote = conditionNote;
    }
}
 
 
 