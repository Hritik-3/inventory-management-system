package com.boot.ordercraft.dto;
 
import java.time.LocalDate;

import java.time.LocalDateTime;

import java.util.List;
 
public class PurchaseOrderDTO {

    private Long poId;
    private LocalDateTime poOrderDate;
    private LocalDate poExpectedDeliveryDate;
    private String poDeliveryStatus;
    private SupplierDTO supplier;
    private UserDto user;
    private List<PurchaseOrderItemDTO> items;
    public PurchaseOrderDTO() {}
 
    // getters and setters
    public Long getPoId() { return poId; }
    public void setPoId(Long poId) { this.poId = poId; }
    public LocalDateTime getPoOrderDate() { return poOrderDate; }
    public void setPoOrderDate(LocalDateTime poOrderDate) { this.poOrderDate = poOrderDate; }
    public LocalDate getPoExpectedDeliveryDate() { return poExpectedDeliveryDate; }

    public void setPoExpectedDeliveryDate(LocalDate poExpectedDeliveryDate) { this.poExpectedDeliveryDate = poExpectedDeliveryDate; }
 
    public String getPoDeliveryStatus() { return poDeliveryStatus; }

    public void setPoDeliveryStatus(String poDeliveryStatus) { this.poDeliveryStatus = poDeliveryStatus; }
 
    public SupplierDTO getSupplier() { return supplier; }

    public void setSupplier(SupplierDTO supplier) { this.supplier = supplier; }
 
    public UserDto getUser() { return user; }

    public void setUser(UserDto user) { this.user = user; }
 
    public List<PurchaseOrderItemDTO> getItems() { return items; }

    public void setItems(List<PurchaseOrderItemDTO> items) { this.items = items; }

}

 