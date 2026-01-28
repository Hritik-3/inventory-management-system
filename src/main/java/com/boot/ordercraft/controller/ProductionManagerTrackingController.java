package com.boot.ordercraft.controller;



import com.boot.ordercraft.dto.ScheduleTrackingDTO;
import com.boot.ordercraft.service.ProductionManagerTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/production")
@CrossOrigin(origins = "http://localhost:4200") // Angular frontend origin
public class ProductionManagerTrackingController {

    @Autowired
    private ProductionManagerTrackingService trackingService;

    // Track schedule by ID
    @GetMapping("/track-schedule/{scheduleId}")
    public ResponseEntity<ScheduleTrackingDTO> trackSchedule(@PathVariable Long scheduleId) {
        ScheduleTrackingDTO trackingDTO = trackingService.trackSchedule(scheduleId);
        return ResponseEntity.ok(trackingDTO);
    }
}

