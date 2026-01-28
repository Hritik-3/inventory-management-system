package com.boot.ordercraft.dto;

public class ProcurementDashboardOverviewDTO {

    private long totalPurchasedItems;
    private long totalReturnedItems;
    private double returnPercentage;

    public ProcurementDashboardOverviewDTO() {}

    public ProcurementDashboardOverviewDTO(long totalPurchasedItems, long totalReturnedItems, double returnPercentage) {
        this.totalPurchasedItems = totalPurchasedItems;
        this.totalReturnedItems = totalReturnedItems;
        this.returnPercentage = returnPercentage;
    }

    public long getTotalPurchasedItems() {
        return totalPurchasedItems;
    }

    public void setTotalPurchasedItems(long totalPurchasedItems) {
        this.totalPurchasedItems = totalPurchasedItems;
    }

    public long getTotalReturnedItems() {
        return totalReturnedItems;
    }

    public void setTotalReturnedItems(long totalReturnedItems) {
        this.totalReturnedItems = totalReturnedItems;
    }

    public double getReturnPercentage() {
        return returnPercentage;
    }

    public void setReturnPercentage(double returnPercentage) {
        this.returnPercentage = returnPercentage;
    }
}
