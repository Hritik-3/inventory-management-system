//productstockview.java
package com.boot.ordercraft.dto;
 
public class ProductStockView {
 
	private Long productId;
    private String productName;
    private int availableQuantity;
    private int minThreshold;
    private int maxThreshold;
    private String alert;
    
	public ProductStockView(Long productId, String productName, int availableQuantity, int minThreshold,
			int maxThreshold, String alert) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.availableQuantity = availableQuantity;
		this.minThreshold = minThreshold;
		this.maxThreshold = maxThreshold;
		this.alert = alert;
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
	public int getAvailableQuantity() {
		return availableQuantity;
	}
	public void setAvailableQuantity(int availableQuantity) {
		this.availableQuantity = availableQuantity;
	}
	public int getMinThreshold() {
		return minThreshold;
	}
	public void setMinThreshold(int minThreshold) {
		this.minThreshold = minThreshold;
	}
	public int getMaxThreshold() {
		return maxThreshold;
	}
	public void setMaxThreshold(int maxThreshold) {
		this.maxThreshold = maxThreshold;
	}
	public String getAlert() {
		return alert;
	}
	public void setAlert(String alert) {
		this.alert = alert;
	}
   
}
 
 
 
 