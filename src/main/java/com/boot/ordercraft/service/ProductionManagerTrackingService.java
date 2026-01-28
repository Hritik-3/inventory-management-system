package com.boot.ordercraft.service;

import com.boot.ordercraft.dto.ScheduleTrackingDTO;
import com.boot.ordercraft.model.ProductionSchedule;
import com.boot.ordercraft.repository.ProductionScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductionManagerTrackingService {

    @Autowired
    private ProductionScheduleRepository scheduleRepository;

    public ScheduleTrackingDTO trackSchedule(Long scheduleId) {
        Optional<ProductionSchedule> scheduleOpt = scheduleRepository.findById(scheduleId);

        if (scheduleOpt.isEmpty()) {
            throw new RuntimeException("Schedule with ID " + scheduleId + " not found");
        }

        ProductionSchedule schedule = scheduleOpt.get();

        // Use correct Product getters
        Long productId = schedule.getProduct() != null ? schedule.getProduct().getProductsId() : null;
        String productName = schedule.getProduct() != null ? schedule.getProduct().getProductsName() : null;

        // ProductionLine getters (assuming lineId and lineName exist)
        Long lineId = schedule.getProductionLine() != null ? schedule.getProductionLine().getLineId() : null;
        String lineName = schedule.getProductionLine() != null ? schedule.getProductionLine().getLineName() : null;

        return new ScheduleTrackingDTO(
                schedule.getPsId(),
                schedule.getPsStatus(),
                schedule.getPsStartDate(),
                schedule.getPsEndDate(),
                schedule.getPsQuantity(),
                productId,
                productName,
                lineId,
                lineName
        );
    }
}
