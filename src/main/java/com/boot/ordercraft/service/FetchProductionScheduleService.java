package com.boot.ordercraft.service;

import com.boot.ordercraft.model.ProductionSchedule;
import com.boot.ordercraft.repository.ProductionScheduleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FetchProductionScheduleService {

    private final ProductionScheduleRepository scheduleRepo;

    public FetchProductionScheduleService(ProductionScheduleRepository scheduleRepo) {
        this.scheduleRepo = scheduleRepo;
    }

    // ✅ Fetch all production schedules
    public List<ProductionSchedule> getAllSchedules() {
        return scheduleRepo.findAll();
    }
}
