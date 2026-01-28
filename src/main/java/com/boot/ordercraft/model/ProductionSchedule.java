package com.boot.ordercraft.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "PRODUCTION_SCHEDULE")
public class ProductionSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "production_schedule_seq")
    @SequenceGenerator(
        name = "production_schedule_seq",
        sequenceName = "PRODUCTION_SCHEDULE_SEQ",
        allocationSize = 1,
        initialValue = 1
    )
    @Column(name = "PSID")
    private Long psId;

    @ManyToOne
    @JoinColumn(name = "PSPRODUCTID", referencedColumnName = "PRODUCTSID")
    private Product product;

    @Temporal(TemporalType.DATE)
    @Column(name = "PSSTARTDATE")
    private Date psStartDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "PSENDDATE")
    private Date psEndDate;

    @Column(name = "PSQUANTITY")
    private Integer psQuantity;

    @Column(name = "PSSTATUS", nullable = false)
    private String psStatus = "PLANNED"; // default value

    @Column(name = "ACTIONS")
    private String actions = "PENDING"; // default

    // ✅ Add ProductionLine relationship
    @ManyToOne
    @JoinColumn(name = "PSPRODUCTIONLINEID", referencedColumnName = "LINE_ID")
    private ProductionLine productionLine;

    // ----------------- Getters & Setters -----------------
    public Long getPsId() {
        return psId;
    }

    public void setPsId(Long psId) {
        this.psId = psId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Date getPsStartDate() {
        return psStartDate;
    }

    public void setPsStartDate(Date psStartDate) {
        this.psStartDate = psStartDate;
    }

    public Date getPsEndDate() {
        return psEndDate;
    }

    public void setPsEndDate(Date psEndDate) {
        this.psEndDate = psEndDate;
    }

    public Integer getPsQuantity() {
        return psQuantity;
    }

    public void setPsQuantity(Integer psQuantity) {
        this.psQuantity = psQuantity;
    }

    public String getPsStatus() {
        return psStatus;
    }

    public void setPsStatus(String psStatus) {
        this.psStatus = psStatus;
    }

    public String getActions() {
        return actions;
    }

    public void setActions(String actions) {
        this.actions = actions;
    }

    public ProductionLine getProductionLine() {
        return productionLine;
    }

    public void setProductionLine(ProductionLine productionLine) {
        this.productionLine = productionLine;
    }
}
