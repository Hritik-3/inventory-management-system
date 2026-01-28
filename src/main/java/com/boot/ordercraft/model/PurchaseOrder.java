package com.boot.ordercraft.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "PURCHASE_ORDERS")
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "purchase_order_seq")
    @SequenceGenerator(
        name = "purchase_order_seq",
        sequenceName = "PURCHASE_ORDER_SEQ",
        allocationSize = 1,
        initialValue = 1
    )
    @Column(name = "POID")
    private Long poId;

    @Column(name = "POORDERDATE")
    private LocalDateTime poOrderDate;

    @Column(name = "POEXPECTEDDELIVERY_DATE")
    private LocalDate poExpectedDelivery_date;

    @Column(name = "PODELIVERYSTATUS")
    private String poDeliveryStatus;

    // ✅ Added new column for order type (no renaming done)
    @Column(name = "POORDERTYPE")
    private String poOrderType;

    @ManyToOne
    @JoinColumn(name = "POSUPPLIERID", referencedColumnName = "SUPPLIERSID")
    private Supplier supplier;

    @ManyToOne
    @JoinColumn(name = "POUSERID", referencedColumnName = "USERID")
    private User user;

    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseOrderItem> items;

    @ManyToOne
    @JoinColumn(name = "POCUSTID", referencedColumnName = "CUST_ID", foreignKey = @ForeignKey(name = "FK_PO_CUSTOMER"))
    private Customer customer;

    // ---------------- Getters & Setters ---------------- //

    public Long getPoId() {
        return poId;
    }

    public void setPoId(Long poId) {
        this.poId = poId;
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

    public void setPoExpectedDelivery_date(LocalDate localDate) {
        this.poExpectedDelivery_date = localDate;
    }

    public String getPoDeliveryStatus() {
        return poDeliveryStatus;
    }

    public void setPoDeliveryStatus(String poDeliveryStatus) {
        this.poDeliveryStatus = poDeliveryStatus;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
        setPoOrderTypeAutomatically(); // ✅ auto-detect order type
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<PurchaseOrderItem> getItems() {
        return items;
    }

    public void setItems(List<PurchaseOrderItem> items) {
        this.items = items;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        setPoOrderTypeAutomatically(); // ✅ auto-detect order type
    }

    public String getPoOrderType() {
        return poOrderType;
    }

    public void setPoOrderType(String poOrderType) {
        this.poOrderType = poOrderType;
    }

    // ✅ Auto-assign order type before saving or updating
    @PrePersist
    @PreUpdate
    private void setPoOrderTypeAutomatically() {
        if (this.poOrderType != null && !"UNKNOWN".equalsIgnoreCase(this.poOrderType)) {
            return; // ✅ respect manually set value
        }
        if (customer != null && supplier == null) {
            this.poOrderType = "CUSTOMER_ORDER";
        } else if (supplier != null && customer == null) {
            this.poOrderType = "SUPPLIER_ORDER";
        } else {
            this.poOrderType = "UNKNOWN";
        }
    }

}
