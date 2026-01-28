package com.boot.ordercraft.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "INVENTORY_TRANSACTION")
public class InventoryTransaction {

    @Id

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "inventory_transaction_seq")
    @SequenceGenerator(
        name = "inventory_transaction_seq",
        sequenceName = "INVENTORY_TRANSACTION_SEQ",
        allocationSize = 1,
        initialValue = 1
    )

    @Column(name = "ITID")
    private Long itId;

    @ManyToOne
    @JoinColumn(name = "ITPRODUCTID", referencedColumnName = "PRODUCTSID")
    private Product product;

    @Column(name = "ITPERFORMEDBY")
    private String itPerformedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ITTRANSACTIONDATE")
    private Date itTransactionDate;

    @Column(name = "ITTRANSACTIONTYPE")
    private String itTransactionType; // "IN" or "OUT"

    @Column(name = "ITQUANTITY")
    private Integer itQuantity;

    public Long getItId() {
        return itId;
    }

    public void setItId(Long itId) {
        this.itId = itId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getItPerformedBy() {
        return itPerformedBy;
    }

    public void setItPerformedBy(String itPerformedBy) {
        this.itPerformedBy = itPerformedBy;
    }

    public Date getItTransactionDate() {
        return itTransactionDate;
    }

    public void setItTransactionDate(Date itTransactionDate) {
        this.itTransactionDate = itTransactionDate;
    }

    public String getItTransactionType() {
        return itTransactionType;
    }

    public void setItTransactionType(String itTransactionType) {
        this.itTransactionType = itTransactionType;
    }

    public Integer getItQuantity() {
        return itQuantity;
    }

    public void setItQuantity(Integer itQuantity) {
        this.itQuantity = itQuantity;
    }
}
