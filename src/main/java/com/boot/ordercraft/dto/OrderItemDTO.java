package com.boot.ordercraft.dto;
 
public class OrderItemDTO {
    private String productName;
    private String category;
    private int quantity;
    private double unitPrice;
 
    public OrderItemDTO() {}
 
    public OrderItemDTO(String productName, String category, int quantity, double unitPrice) {
        this.productName = productName;
        this.category = category;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }
 
    // Getters and Setters
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
 
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
 
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
 
    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }
}
 
 