package com.boot.ordercraft.service;

import com.boot.ordercraft.dto.ProcurementDashboardOverviewDTO;
import com.boot.ordercraft.dto.ProcurementDashboardStatsDTO;
import com.boot.ordercraft.repository.PurchaseOrderItemsRepository;
import com.boot.ordercraft.repository.ReturnOrderItemsRepository;

import org.springframework.stereotype.Service;

@Service
public class ProcurementDashboardService {

    private final PurchaseOrderItemsRepository purchaseOrderItemRepository;
    private final ReturnOrderItemsRepository returnOrderItemRepository;

    public ProcurementDashboardService(PurchaseOrderItemsRepository purchaseOrderItemRepository,
                                       ReturnOrderItemsRepository returnOrderItemRepository) {
        this.purchaseOrderItemRepository = purchaseOrderItemRepository;
        this.returnOrderItemRepository = returnOrderItemRepository;
    }

    // Overview: total purchased, total returned, overall return %
    public ProcurementDashboardOverviewDTO getOverview() {
        long totalPurchased = purchaseOrderItemRepository.getTotalPurchasedItems();
        long totalReturned = returnOrderItemRepository.getTotalReturnedItems();

        double returnPercentage = totalPurchased == 0 ? 0.0 : (totalReturned * 100.0 / totalPurchased);

        return new ProcurementDashboardOverviewDTO(totalPurchased, totalReturned, returnPercentage);
    }

    // Stats endpoint (for /stats)
    public ProcurementDashboardStatsDTO getStats() {
        long totalPurchased = purchaseOrderItemRepository.getTotalPurchasedItems();
        long totalReturned = returnOrderItemRepository.getTotalReturnedItems();
        double returnPercentage = totalPurchased == 0 ? 0.0 : (totalReturned * 100.0 / totalPurchased);

        return new ProcurementDashboardStatsDTO(totalPurchased, totalReturned, returnPercentage);
    }

}
