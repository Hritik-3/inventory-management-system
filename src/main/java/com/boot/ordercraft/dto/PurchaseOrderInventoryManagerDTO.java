package com.boot.ordercraft.dto;


public class PurchaseOrderInventoryManagerDTO {
    private Long poiId;
    private Integer poiQuantity;
    private Float poiCost;
    private String productName; // Optional: from Product
    private Long productId;     // Optional: for backend linking

    public PurchaseOrderInventoryManagerDTO() {}

    public PurchaseOrderInventoryManagerDTO(Long poiId, Integer poiQuantity, Float poiCost, String productName, Long productId) {
        this.poiId = poiId;
        this.poiQuantity = poiQuantity;
        this.poiCost = poiCost;
        this.productName = productName;
        this.productId = productId;
    }

    public Long getPoiId() {
        return poiId;
    }

    public void setPoiId(Long poiId) {
        this.poiId = poiId;
    }

    public Integer getPoiQuantity() {
        return poiQuantity;
    }

    public void setPoiQuantity(Integer poiQuantity) {
        this.poiQuantity = poiQuantity;
    }

    public Float getPoiCost() {
        return poiCost;
    }

    public void setPoiCost(Float poiCost) {
        this.poiCost = poiCost;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}

