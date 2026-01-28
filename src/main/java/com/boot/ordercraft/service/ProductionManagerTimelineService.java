package com.boot.ordercraft.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.boot.ordercraft.dto.TimelineEventDTO;
import com.boot.ordercraft.dto.UnitLineStatusDTO;
import com.boot.ordercraft.model.ProductionLine;
import com.boot.ordercraft.repository.ProductionLineRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductionManagerTimelineService {

    @Autowired
    private ProductionLineRepository productionLineRepository;

    // ✅ Existing mock timeline method
    public List<TimelineEventDTO> getTimeline(Long scheduleId) {
        if (scheduleId == 66L) {
            List<TimelineEventDTO> timeline = new ArrayList<>();
            timeline.add(new TimelineEventDTO(LocalDateTime.now().minusDays(3), "CREATED", "Schedule created by manager"));
            timeline.add(new TimelineEventDTO(LocalDateTime.now().minusDays(2), "MATERIALS_ALLOCATED", "Raw materials allocated"));
            timeline.add(new TimelineEventDTO(LocalDateTime.now().minusDays(1), "IN_PROGRESS", "Production started"));
            timeline.add(new TimelineEventDTO(LocalDateTime.now(), "COMPLETED", "Production completed successfully"));
            return timeline;
        }
        return new ArrayList<>();
    }

    // ✅ Fetch unit & line statuses from DB
    public List<UnitLineStatusDTO> getUnitsStatus() {
        List<ProductionLine> lines = productionLineRepository.findAllByOrderByLineNameAsc();
        List<UnitLineStatusDTO> response = new ArrayList<>();

        for (ProductionLine line : lines) {
            response.add(new UnitLineStatusDTO(
                line.getProductionUnit().getUnitName(),
                line.getLineName(),
                line.getStatus()
            ));
        }

        return response;
    }
}
