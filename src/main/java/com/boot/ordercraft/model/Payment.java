package com.boot.ordercraft.model;

import jakarta.persistence.*;
import java.util.Date;
@Entity

@Table(name = "ORDERS")
 
public class Payment {
 
    @Id
 
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment_seq")
    @SequenceGenerator(
        name = "payment_seq",
        sequenceName = "PAYMENT_SEQ",
        allocationSize = 1,
        initialValue = 1
    )
 
    private Long id;
 
    @Column(name = "ORDER_ID")
 
    private Long orderId;
 
    @Column(name = "AMOUNT")
 
    private Double amount;
 
    @Column(name = "PAYMENT_DATE")
 
    @Temporal(TemporalType.TIMESTAMP)
 
    private Date paymentDate;
 
    @Column(name = "STATUS")
 
    private String status;
 
    public Long getId() { return id; }
 
    public void setId(Long id) { this.id = id; }
 
    public Long getOrderId() { return orderId; }
 
    public void setOrderId(Long orderId) { this.orderId = orderId; }
 
    public Double getAmount() { return amount; }
 
    public void setAmount(Double amount) { this.amount = amount; }
 
    public Date getPaymentDate() { return paymentDate; }
 
    public void setPaymentDate(Date paymentDate) { this.paymentDate = paymentDate; }
 
    public String getStatus() { return status; }
 
    public void setStatus(String status) { this.status = status; }
 
}
 
 