package com.boot.ordercraft.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.boot.ordercraft.dto.DashboardDTO;
import com.boot.ordercraft.service.InventoryDashboardService;


@RestController
@RequestMapping("/api/dashboard")
public class InventoryDashboardController {
	
	   @Autowired
	    private InventoryDashboardService dashboardService;

	    @GetMapping
	    public DashboardDTO getDashboardData() {
	        return dashboardService.getDashboardData();
	 
}
}
