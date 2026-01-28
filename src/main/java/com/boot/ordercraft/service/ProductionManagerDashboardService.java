package com.boot.ordercraft.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.boot.ordercraft.dto.ProductionManagerDashboardDTO;
import com.boot.ordercraft.model.*;
import com.boot.ordercraft.repository.*;

@Service
public class ProductionManagerDashboardService {

    private final ProductsRepository productRepository;
    private final RawMaterialsRepository rawMaterialRepository;
    private final ProductionLineRepository productionLineRepository;
    private final ProductionScheduleRepository productionScheduleRepository;
    private final ProductRawMaterialRepository productRawMaterialRepository;

    public ProductionManagerDashboardService(
            ProductsRepository productRepository,
            RawMaterialsRepository rawMaterialRepository,
            ProductionLineRepository productionLineRepository,
            ProductionScheduleRepository productionScheduleRepository,
            ProductRawMaterialRepository productRawMaterialRepository) {
        this.productRepository = productRepository;
        this.rawMaterialRepository = rawMaterialRepository;
        this.productionLineRepository = productionLineRepository;
        this.productionScheduleRepository = productionScheduleRepository;
        this.productRawMaterialRepository = productRawMaterialRepository;
    }

    public ProductionManagerDashboardDTO getDashboardData() {

        ProductionManagerDashboardDTO dto = new ProductionManagerDashboardDTO();

        dto.setProducts(productRepository.findAll());
        dto.setRawMaterials(rawMaterialRepository.findAll());
        dto.setProductionLines(productionLineRepository.findAll());
        dto.setProductionSchedules(productionScheduleRepository.findAll());
        dto.setProductRawMaterials(productRawMaterialRepository.findAll());

        return dto;
    }
}
