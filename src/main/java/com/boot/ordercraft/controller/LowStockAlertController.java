package com.boot.ordercraft.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.boot.ordercraft.model.LowStockAlert;
import com.boot.ordercraft.service.LowStockAlertService;

@RestController
@RequestMapping("/api/low-stock-alerts")
@CrossOrigin(origins = "*")
public class LowStockAlertController {
	
	private final LowStockAlertService alertService;
	 
    public LowStockAlertController(LowStockAlertService alertService) {
        this.alertService = alertService;
    }
 
    @GetMapping
    public ResponseEntity<List<LowStockAlert>> getAllAlerts() {
        return ResponseEntity.ok(alertService.getAllAlerts());
    }
 
    @PostMapping("/acknowledge/{id}")
    public ResponseEntity<LowStockAlert> acknowledge(@PathVariable Long id) {
        return ResponseEntity.ok(alertService.acknowledgeAlert(id));
    }
 
    @PostMapping("/check")
    public ResponseEntity<String> checkAlerts() {
        alertService.checkAndGenerateAlerts();
        return ResponseEntity.ok("Checked and generated alerts if any");

}
}
