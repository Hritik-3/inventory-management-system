package com.boot.ordercraft.dto;

public class ProcurementDashboardStatsDTO {
    private long totalPurchased;
    private long totalReturned;
    private double returnPercentage;

    public ProcurementDashboardStatsDTO() {}

    public ProcurementDashboardStatsDTO(long totalPurchased, long totalReturned, double returnPercentage) {
        this.totalPurchased = totalPurchased;
        this.totalReturned = totalReturned;
        this.returnPercentage = returnPercentage;
    }

    public long getTotalPurchased() { return totalPurchased; }
    public void setTotalPurchased(long totalPurchased) { this.totalPurchased = totalPurchased; }

    public long getTotalReturned() { return totalReturned; }
    public void setTotalReturned(long totalReturned) { this.totalReturned = totalReturned; }

    public double getReturnPercentage() { return returnPercentage; }
    public void setReturnPercentage(double returnPercentage) { this.returnPercentage = returnPercentage; }
}
