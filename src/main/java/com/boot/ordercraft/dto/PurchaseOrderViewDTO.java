package com.boot.ordercraft.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class PurchaseOrderViewDTO {
    private Long poId;
    private String poDeliveryStatus;
    private LocalDateTime poOrderDate;
    private LocalDate poExpectedDelivery_date;
    private String poOrderType;
    private String customerName;
    private String supplierName;
    private List<PurchaseOrderInventoryManagerDTO> items;

    public Long getPoId() {
        return poId;
    }

    public void setPoId(Long poId) {
        this.poId = poId;
    }

    public String getPoDeliveryStatus() {
        return poDeliveryStatus;
    }

    public void setPoDeliveryStatus(String poDeliveryStatus) {
        this.poDeliveryStatus = poDeliveryStatus;
    }

    public LocalDateTime getPoOrderDate() {
        return poOrderDate;
    }

    public void setPoOrderDate(LocalDateTime poOrderDate) {
        this.poOrderDate = poOrderDate;
    }

    public LocalDate getPoExpectedDelivery_date() {
        return poExpectedDelivery_date;
    }

    public void setPoExpectedDelivery_date(LocalDate poExpectedDelivery_date) {
        this.poExpectedDelivery_date = poExpectedDelivery_date;
    }

    public String getPoOrderType() {
        return poOrderType;
    }

    public void setPoOrderType(String poOrderType) {
        this.poOrderType = poOrderType;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public List<PurchaseOrderInventoryManagerDTO> getItems() {
        return items;
    }

    public void setItems(List<PurchaseOrderInventoryManagerDTO> items) {
        this.items = items;
    }
}
