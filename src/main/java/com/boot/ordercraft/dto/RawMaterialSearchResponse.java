package com.boot.ordercraft.dto;
 
import com.boot.ordercraft.model.RawMaterial;
 
public class RawMaterialSearchResponse {
    private Long id;
    private String name;
    private int quantity;
    private String supplierName;
    
    
 
    public Long getId() {
		return id;
	}
 
 
 
	public void setId(Long id) {
		this.id = id;
	}
 
 
 
	public String getName() {
		return name;
	}
 
 
 
	public void setName(String name) {
		this.name = name;
	}
 
 
 
	public int getQuantity() {
		return quantity;
	}
 
 
 
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
 
 
 
	public String getSupplierName() {
		return supplierName;
	}
 
 
 
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
 
 
 
	public RawMaterialSearchResponse(RawMaterial rm) {
        this.id = rm.getRwId();
        this.name = rm.getRwName();
        this.quantity = rm.getRwQuantity();
        this.supplierName = rm.getSupplier() != null ? rm.getSupplier().getSuppliersName() : null;
    }
}
 
 