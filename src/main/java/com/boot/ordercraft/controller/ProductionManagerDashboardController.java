package com.boot.ordercraft.controller;

import org.springframework.web.bind.annotation.*;

import com.boot.ordercraft.dto.ProductionManagerDashboardDTO;
import com.boot.ordercraft.service.ProductionManagerDashboardService;

@RestController
@RequestMapping("/api/production-manager")
@CrossOrigin("*")
public class ProductionManagerDashboardController {

    private final ProductionManagerDashboardService dashboardService;

    public ProductionManagerDashboardController(ProductionManagerDashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/dashboard")
    public ProductionManagerDashboardDTO getDashboard() {
        return dashboardService.getDashboardData();
    }
}
