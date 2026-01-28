package com.boot.ordercraft.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "SUPPLIER_PAYMENTS")
public class SupplierPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "supplier_payment_seq")
    @SequenceGenerator(name = "supplier_payment_seq", sequenceName = "SUPPLIER_PAYMENT_SEQ", allocationSize = 1)
    @Column(name = "PAYMENT_ID")
    private Long paymentId;

    // Supplier Relationship
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SUPPLIER_ID", nullable = false)
    private Supplier supplier;

    // ✅ New field: Link to Purchase Order
    @Column(name = "PO_ID")
    private Long poId;  // temporary, later can be @ManyToOne(PurchaseOrder)

    @Column(name = "AMOUNT_PAID")
    private Double amountPaid;

    @Column(name = "PAYMENT_DATE")
    private LocalDate paymentDate = LocalDate.now();

    @Column(name = "PAYMENT_MODE")
    private String paymentMode;

    @Column(name = "PAYMENT_STATUS")
    private String paymentStatus;

    @Column(name = "REFERENCE_NO")
    private String referenceNo;

    @Column(name = "REMARKS")
    private String remarks;

    // ------------------- Getters & Setters -------------------
    public Long getPaymentId() { return paymentId; }
    public void setPaymentId(Long paymentId) { this.paymentId = paymentId; }

    public Supplier getSupplier() { return supplier; }
    public void setSupplier(Supplier supplier) { this.supplier = supplier; }

    public Long getPoId() { return poId; }
    public void setPoId(Long poId) { this.poId = poId; }

    public Double getAmountPaid() { return amountPaid; }
    public void setAmountPaid(Double amountPaid) { this.amountPaid = amountPaid; }

    public LocalDate getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDate paymentDate) { this.paymentDate = paymentDate; }

    public String getPaymentMode() { return paymentMode; }
    public void setPaymentMode(String paymentMode) { this.paymentMode = paymentMode; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public String getReferenceNo() { return referenceNo; }
    public void setReferenceNo(String referenceNo) { this.referenceNo = referenceNo; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}
