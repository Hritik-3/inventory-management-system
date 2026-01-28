package com.boot.ordercraft.controller;

import com.boot.ordercraft.dto.ProcurementDashboardOverviewDTO;
import com.boot.ordercraft.dto.ProcurementDashboardStatsDTO;
import com.boot.ordercraft.service.ProcurementDashboardService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/procurement-dashboard")
@CrossOrigin(origins = "*")
public class ProcurementDashboardController {

    private final ProcurementDashboardService procurementDashboardService;

    public ProcurementDashboardController(ProcurementDashboardService procurementDashboardService) {
        this.procurementDashboardService = procurementDashboardService;
    }

    @GetMapping("/overview")
    public ProcurementDashboardOverviewDTO getOverview() {
        return procurementDashboardService.getOverview();
    }

    @GetMapping("/stats")
    public ProcurementDashboardStatsDTO getStats() {
        return procurementDashboardService.getStats();
    }
}
