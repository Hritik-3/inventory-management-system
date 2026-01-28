package com.boot.ordercraft.controller;

import com.boot.ordercraft.dto.DashboardStatsDTO;
import com.boot.ordercraft.service.AdminDashboardService;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
public class AdminDashboardController {

    private final AdminDashboardService dashboardService;

    public AdminDashboardController(AdminDashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/dashboard-overview")
    public DashboardStatsDTO getDashboardOverview() {
        return dashboardService.getDashboardStats();
    }
}
