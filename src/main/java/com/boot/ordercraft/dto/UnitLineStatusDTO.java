package com.boot.ordercraft.dto;


public class UnitLineStatusDTO {
    private String unitName;
    private String lineName;
    private String status;

    public UnitLineStatusDTO() {}

    public UnitLineStatusDTO(String unitName, String lineName, String status) {
        this.unitName = unitName;
        this.lineName = lineName;
        this.status = status;
    }

    // Getters & Setters
    public String getUnitName() { return unitName; }
    public void setUnitName(String unitName) { this.unitName = unitName; }

    public String getLineName() { return lineName; }
    public void setLineName(String lineName) { this.lineName = lineName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

