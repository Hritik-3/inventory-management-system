package com.boot.ordercraft.dto;


public class PurchaseOrderItemDTO {
    private Long itemId;  // assuming your PurchaseOrderItem has an ID field
    private Integer quantity;
    private Float cost;
    private ProductDTO product;
 
    public PurchaseOrderItemDTO() {}
 
    public PurchaseOrderItemDTO(Long itemId, Integer quantity, Float cost, ProductDTO product) {
        this.itemId = itemId;
        this.quantity = quantity;
        this.cost = cost;
        this.product = product;
    }
 
    // getters and setters
    public Long getItemId() { return itemId; }
    public void setItemId(Long itemId) { this.itemId = itemId; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public Float getCost() { return cost; }
    public void setCost(Float cost) { this.cost = cost; }
    public ProductDTO getProduct() { return product; }
    public void setProduct(ProductDTO product) { this.product = product; }
}
 
 