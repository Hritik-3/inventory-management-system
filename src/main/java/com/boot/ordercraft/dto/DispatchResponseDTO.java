package com.boot.ordercraft.dto;

import java.util.List;

public class DispatchResponseDTO {
    private Long purchaseOrderId;
    private String message;
    private List<ItemDTO> dispatchedItems;

  
    public static class ItemDTO {
        private Long productId;
        private Integer quantity;

        public ItemDTO() { }                       // needed by Jackson
        public ItemDTO(Long productId, Integer quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }

        // getters & setters
        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
    }


	public Long getPurchaseOrderId() {
		return purchaseOrderId;
	}


	public void setPurchaseOrderId(Long purchaseOrderId) {
		this.purchaseOrderId = purchaseOrderId;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public List<ItemDTO> getDispatchedItems() {
		return dispatchedItems;
	}


	public void setDispatchedItems(List<ItemDTO> dispatchedItems) {
		this.dispatchedItems = dispatchedItems;
	}
}
