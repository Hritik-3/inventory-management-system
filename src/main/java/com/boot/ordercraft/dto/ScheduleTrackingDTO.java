package com.boot.ordercraft.dto;

import java.util.Date;

public class ScheduleTrackingDTO {

    private Long psId;
    private String psStatus;
    private Date psStartDate;
    private Date psEndDate;
    private Integer psQuantity;

    private Long productId;
    private String productName;

    private Long productionLineId;
    private String productionLineName;

    private String actions;

    public ScheduleTrackingDTO() {}

    public ScheduleTrackingDTO(Long psId, String psStatus, Date psStartDate, Date psEndDate, Integer psQuantity,
                               Long productId, String productName,
                               Long productionLineId, String productionLineName)
//                               String actions) 
                               {
        this.psId = psId;
        this.psStatus = psStatus;
        this.psStartDate = psStartDate;
        this.psEndDate = psEndDate;
        this.psQuantity = psQuantity;
        this.productId = productId;
        this.productName = productName;
        this.productionLineId = productionLineId;
        this.productionLineName = productionLineName;
        //this.actions = actions;
    }

    // ----------------- Getters & Setters -----------------
    public Long getPsId() { return psId; }
    public void setPsId(Long psId) { this.psId = psId; }

    public String getPsStatus() { return psStatus; }
    public void setPsStatus(String psStatus) { this.psStatus = psStatus; }

    public Date getPsStartDate() { return psStartDate; }
    public void setPsStartDate(Date psStartDate) { this.psStartDate = psStartDate; }

    public Date getPsEndDate() { return psEndDate; }
    public void setPsEndDate(Date psEndDate) { this.psEndDate = psEndDate; }

    public Integer getPsQuantity() { return psQuantity; }
    public void setPsQuantity(Integer psQuantity) { this.psQuantity = psQuantity; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public Long getProductionLineId() { return productionLineId; }
    public void setProductionLineId(Long productionLineId) { this.productionLineId = productionLineId; }

    public String getProductionLineName() { return productionLineName; }
    public void setProductionLineName(String productionLineName) { this.productionLineName = productionLineName; }

    public String getActions() { return actions; }
    public void setActions(String actions) { this.actions = actions; }
}
