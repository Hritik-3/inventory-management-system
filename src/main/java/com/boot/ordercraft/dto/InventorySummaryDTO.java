package com.boot.ordercraft.dto;
 
 
public class InventorySummaryDTO {
 
    private int totalProducts;
    private int totalQuantity;
    private long lowStockCount;
 
    public InventorySummaryDTO() {}
 
    public InventorySummaryDTO(int totalProducts, int totalQuantity, long lowStockCount) {
        this.totalProducts = totalProducts;
        this.totalQuantity = totalQuantity;
        this.lowStockCount = lowStockCount;
    }
 
    public int getTotalProducts() {
        return totalProducts;
    }
 
    public void setTotalProducts(int totalProducts) {
        this.totalProducts = totalProducts;
    }
 
    public int getTotalQuantity() {
        return totalQuantity;
    }
 
    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }
 
    public long getLowStockCount() {
        return lowStockCount;
    }
 
    public void setLowStockCount(long lowStockCount) {
        this.lowStockCount = lowStockCount;
    }
}
 