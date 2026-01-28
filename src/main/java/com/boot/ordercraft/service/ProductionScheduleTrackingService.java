package com.boot.ordercraft.service;
 
import com.boot.ordercraft.dto.ProductionScheduleTrackingResponse;
import com.boot.ordercraft.model.ProductionSchedule;
import com.boot.ordercraft.model.Product;
import com.boot.ordercraft.repository.ProductionScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
 
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
 
@Service
public class ProductionScheduleTrackingService {
 
    @Autowired
    private ProductionScheduleRepository scheduleRepository;
 
    // Map entity to response DTO
    private ProductionScheduleTrackingResponse mapToTrackingResponse(ProductionSchedule ps) {
        ProductionScheduleTrackingResponse dto = new ProductionScheduleTrackingResponse();
        dto.setScheduleId(ps.getPsId());
 
        // ✅ Always use relation
        
        //changes 
        
        
        Product product = ps.getProduct();
        String productName = (product != null) ? product.getProductsName() : "Unknown";
 
        dto.setProductId(product != null ? product.getProductsId() : null);
        dto.setProductName(productName);
        dto.setQuantity(ps.getPsQuantity());
        dto.setStartDate(ps.getPsStartDate());
        dto.setEndDate(ps.getPsEndDate());
        dto.setStatus(ps.getPsStatus());
//        dto.setActions(ps.getActions());   // ✅ Added mapping for actions
 
        return dto;
    }
 
    // View all production orders
    public List<ProductionScheduleTrackingResponse> getAllProductionOrders() {
        return scheduleRepository.findAllByOrderByPsIdDesc()
                .stream()
                .map(this::mapToTrackingResponse)
                .collect(Collectors.toList());
    }
 
    // View production order by ID
    public Optional<ProductionScheduleTrackingResponse> getProductionOrderById(Long id) {
        return scheduleRepository.findById(id)
                .map(this::mapToTrackingResponse);
    }
 
    @Async
    public void trackOrderStatus(Long scheduleId) {
        System.out.println("Async started for Schedule ID: " + scheduleId);
        try {
            // 1️⃣ After 1 minute -> IN_PROGRESS
           // Thread.sleep(1 * 60 * 1000);
            Thread.sleep(20 * 1000);
            System.out.println("Changing to IN_PROGRESS for Schedule ID: " + scheduleId);
            updateStatus(scheduleId, "IN_PROGRESS");
 
            // 2️⃣ After additional 1 minute -> DISPATCHED
           // Thread.sleep(1 * 60 * 1000);
            Thread.sleep(20 * 1000);
            System.out.println("Changing to DISPATCHED for Schedule ID: " + scheduleId);
            updateStatus(scheduleId, "DISPATCHED");
 
            // 3️⃣ After additional 1 minute -> COMPLETED
           // Thread.sleep(1 * 60 * 1000);
            Thread.sleep(20 * 1000);
            System.out.println("Changing to COMPLETED for Schedule ID: " + scheduleId);
            updateStatus(scheduleId, "COMPLETED");
 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Tracking interrupted for Schedule ID: " + scheduleId);
        }
    }
 
    // Helper method to update status
    private void updateStatus(Long scheduleId, String status) {
        scheduleRepository.findById(scheduleId).ifPresent(schedule -> {
            schedule.setPsStatus(status);
 
            // ✅ When status becomes COMPLETED, set today's date as end date
            if ("COMPLETED".equals(status)) {
            	schedule.setPsEndDate(new Date());
                // Keep actions as "PENDING" until frontend updates
                if (schedule.getActions() == null) {
                    schedule.setActions("PENDING");
                }
            }
 
            scheduleRepository.save(schedule);
            System.out.println("Schedule " + scheduleId + " updated to " + status);
        });
    }
 
    // ✅ New method to update actions (RECEIVED / NOT_RECEIVED)
    public void updateActions(Long scheduleId, String action) {
        ProductionSchedule ps = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Schedule not found: " + scheduleId));
 
        if (!"COMPLETED".equals(ps.getPsStatus())) {
            throw new RuntimeException("Actions can only be updated after COMPLETED status");
        }
 
        ps.setActions(action);
        scheduleRepository.save(ps);
        System.out.println("Schedule " + scheduleId + " actions updated to " + action);
    }
}
 
 