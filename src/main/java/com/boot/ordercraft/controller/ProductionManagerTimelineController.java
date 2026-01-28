package com.boot.ordercraft.controller;


import com.boot.ordercraft.dto.TimelineEventDTO;
import com.boot.ordercraft.dto.UnitLineStatusDTO;
import com.boot.ordercraft.service.ProductionManagerTimelineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/production")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductionManagerTimelineController {

    @Autowired
    private ProductionManagerTimelineService timelineService;

    // 🔹 Get timeline for a specific schedule
    @GetMapping("/timeline/{scheduleId}")
    public ResponseEntity<List<TimelineEventDTO>> getTimeline(@PathVariable Long scheduleId) {
        List<TimelineEventDTO> timeline = timelineService.getTimeline(scheduleId);
        return ResponseEntity.ok(timeline);
    }

    // 🔹 Get all unit-line statuses
    @GetMapping("/units-status")
    public ResponseEntity<List<UnitLineStatusDTO>> getUnitsStatus() {
        List<UnitLineStatusDTO> unitsStatus = timelineService.getUnitsStatus();
        return ResponseEntity.ok(unitsStatus);
    }
}

