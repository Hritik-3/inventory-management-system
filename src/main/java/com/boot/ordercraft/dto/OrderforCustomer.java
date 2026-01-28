package com.boot.ordercraft.dto;

public class OrderforCustomer {

	
	private Long productsId;
    private Integer quantity;
   
    
    
	public Long getProductsId() {
		return productsId;
	}
	public void setProductsId(Long productsId) {
		this.productsId = productsId;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

    
}
