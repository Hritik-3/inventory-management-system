package com.boot.ordercraft.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class DashboardDTO {
	
	private int totalProducts;
    private BigDecimal stockValue;
    private int lowStockAlerts;
	private int outOfStock;
    private int overstockItems;
    private int warehouseUtilization;
    
    private List<ProductDistributionDTO> productDistribution;
    
    
	public List<ProductDistributionDTO> getProductDistribution() {
		return productDistribution;
	}
	public void setProductDistribution(List<ProductDistributionDTO> productDistribution2) {
		this.productDistribution = productDistribution2;
	}
	public int getTotalProducts() {
		return totalProducts;
	}
	public void setTotalProducts(int totalProducts) {
		this.totalProducts = totalProducts;
	}
	public BigDecimal getStockValue() {
		return stockValue;
	}
	public void setStockValue(BigDecimal stockValue2) {
		this.stockValue = stockValue2;
	}
	public int getLowStockAlerts() {
		return lowStockAlerts;
	}
	public void setLowStockAlerts(int lowStockAlerts) {
		this.lowStockAlerts = lowStockAlerts;
	}
	public int getOutOfStock() {
		return outOfStock;
	}
	public void setOutOfStock(int outOfStock) {
		this.outOfStock = outOfStock;
	}
	public int getOverstockItems() {
		return overstockItems;
	}
	public void setOverstockItems(int overstockItems) {
		this.overstockItems = overstockItems;
	}
	
	 public int getWarehouseUtilization() {
		return warehouseUtilization;
	}
	public void setWarehouseUtilization(int warehouseUtilization) {
		this.warehouseUtilization = warehouseUtilization;
	}
    
}
