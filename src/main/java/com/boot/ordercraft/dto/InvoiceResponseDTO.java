package com.boot.ordercraft.dto;
 
import java.util.List;
 
public class InvoiceResponseDTO {
 
    private Long orderId;
    private Long customerId;
    private String Customername;
    private String billingAddress;
    private String phoneNumber;
    private List<OrderItemDTO> items;
 
    private double subtotal;
    private double tax;
    private double totalPrice;
 
    public InvoiceResponseDTO() {
    }
 
   
    
 
 
	@Override
	public String toString() {
		return "InvoiceResponseDTO [orderId=" + orderId + ", customerId=" + customerId + ", Customername="
				+ Customername + ", billingAddress=" + billingAddress + ", phoneNumber=" + phoneNumber + ", items="
				+ items + "]";
	}
 
 
 
 
 
	public InvoiceResponseDTO(Long orderId, Long customerId, String customername, String billingAddress,
			String phoneNumber, List<OrderItemDTO> items) {
		super();
		this.orderId = orderId;
		this.customerId = customerId;
		this.Customername = customername;
		this.billingAddress = billingAddress;
		this.phoneNumber = phoneNumber;
		this.items = items;
	}
 
 
 
 
 
	// Getters and Setters
 
    public Long getOrderId() {
        return orderId;
    }
 
    public Long getCustomerId() {
		return customerId;
	}
 
 
 
 
 
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
 
 
 
 
 
	public String getCustomername() {
		return Customername;
	}
 
	public void setCustomername(String customername) {
		Customername = customername;
	}
 
	public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
 
    public String getBillingAddress() {
        return billingAddress;
    }
 
    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }
 
    public String getPhoneNumber() {
        return phoneNumber;
    }
 
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
 
    public List<OrderItemDTO> getItems() {
        return items;
    }
 
    public void setItems(List<OrderItemDTO> items) {
        this.items = items;
    }
 
    public double getSubtotal() {
        return subtotal;
    }
 
    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
 
    public double getTax() {
        return tax;
    }
 
    public void setTax(double tax) {
        this.tax = tax;
    }
 
    public double getTotalPrice() {
        return totalPrice;
    }
 
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
 
 