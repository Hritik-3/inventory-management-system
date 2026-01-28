package com.boot.ordercraft.dto;
 
import java.time.LocalDate;
import java.util.*;
import java.util.List;
 
 
 
public class PurchaseOrderInternalRequest {
    
	
	private Long customerId; // The user who needs the products
    
	private String priority;
    private LocalDate expectedDate;
    private String notes;
   
    private List<OrderforCustomer> items;
    
    public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	
	
	
	public LocalDate getExpectedDate() {
		return expectedDate;
	}
	public void setExpectedDate(LocalDate expectedDate) {
		this.expectedDate = expectedDate;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
 
 
 
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public List<OrderforCustomer> getItems() {
		return items;
	}
	public void setItems(List<OrderforCustomer> items) {
		this.items = items;
	}
 
   
}
 
 