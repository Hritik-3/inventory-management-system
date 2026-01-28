package com.boot.ordercraft.service;


import com.boot.ordercraft.dto.UnitLineStatusDTO;
import com.boot.ordercraft.model.ProductionUnit;
import com.boot.ordercraft.model.ProductionLine;
import com.boot.ordercraft.repository.ProductionUnitRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductionUnitService {

    private final ProductionUnitRepository unitRepo;

    public ProductionUnitService(ProductionUnitRepository unitRepo) {
        this.unitRepo = unitRepo;
    }

    public List<UnitLineStatusDTO> getAllUnitLineStatus() {
        List<UnitLineStatusDTO> result = new ArrayList<>();
        List<ProductionUnit> units = unitRepo.findAll();

        for (ProductionUnit unit : units) {
            if (unit.getProductionLines() != null && !unit.getProductionLines().isEmpty()) {
                for (ProductionLine line : unit.getProductionLines()) {
                    result.add(new UnitLineStatusDTO(
                        unit.getUnitName(),
                        line.getLineName(),
                        line.getStatus()
                    ));
                }
            } else {
                // If no lines, just show unit as AVAILABLE
                result.add(new UnitLineStatusDTO(
                    unit.getUnitName(),
                    "No lines",
                    "AVAILABLE"
                ));
            }
        }

        return result;
    }
}

