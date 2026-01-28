package com.boot.ordercraft.controller;
 
import com.boot.ordercraft.dto.ProductionScheduleTrackingResponse;
import com.boot.ordercraft.service.ProductionScheduleTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
 
import java.util.List;
import java.util.Optional;
 
@RestController
@RequestMapping("/api/production-tracking")
public class ProductionScheduleTrackingController {
 
    @Autowired
    private ProductionScheduleTrackingService trackingService;
 
    // View all orders
    @GetMapping
    public List<ProductionScheduleTrackingResponse> getAllProductionOrders() {
        return trackingService.getAllProductionOrders();
    }
 
    // View by ID
    @GetMapping("/{id}")
    public Optional<ProductionScheduleTrackingResponse> getProductionOrderById(@PathVariable Long id) {
        return trackingService.getProductionOrderById(id);
    }
 
    // ✅ Update actions (Received / Not Received)
    @PutMapping("/{id}/actions")
    public String updateActions(@PathVariable Long id, @RequestParam String action) {
        trackingService.updateActions(id, action);
        return "Actions updated to: " + action;
    }
}
 
 