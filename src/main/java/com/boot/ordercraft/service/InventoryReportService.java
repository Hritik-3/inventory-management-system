package com.boot.ordercraft.service;
 
import com.boot.ordercraft.dto.InventoryReportFilterDTO;

import com.boot.ordercraft.dto.StockReportDTO;

import com.boot.ordercraft.dto.InventoryTransactionReportDTO;

import com.boot.ordercraft.dto.ProductionReportDTO;

import com.boot.ordercraft.dto.ProductionReportFilterDTO;

import com.boot.ordercraft.dto.InventorySummaryDTO;

import com.boot.ordercraft.model.InventoryTransaction;

import com.boot.ordercraft.model.Product;

import com.boot.ordercraft.model.ProductionSchedule;

import com.boot.ordercraft.repository.InventoryTransactionRepository;

import com.boot.ordercraft.repository.ProductsRepository;

import com.boot.ordercraft.repository.ProductionScheduleRepository;
import com.boot.ordercraft.repository.ProductsRepository;

import org.springframework.stereotype.Service;
 
import java.time.LocalDateTime;

import java.time.LocalTime;

import java.time.ZoneId;

import java.util.List;

import java.util.stream.Collectors;
 
@Service

public class InventoryReportService {
 
    private final ProductsRepository productRepository;

    private final InventoryTransactionRepository transactionRepository;

    private final ProductionScheduleRepository productionScheduleRepository;
 
    public InventoryReportService(ProductsRepository productRepository,

          InventoryTransactionRepository transactionRepository,

          ProductionScheduleRepository productionScheduleRepository) {

				this.productRepository = productRepository;

				this.transactionRepository = transactionRepository;

				this.productionScheduleRepository = productionScheduleRepository;

				}

    /** 

     * Generate Stock Level Report with optional category and date filters

     */

    public List<StockReportDTO> getStockReport(InventoryReportFilterDTO filter) {

        List<Product> products = (List<Product>) productRepository.findAll();
 
        return products.stream()

                // Filter by category if provided

                .filter(p -> filter.getCategoryId() == null || 

                             (p.getCategory() != null && p.getCategory().getCategoriesId().equals(filter.getCategoryId())))

                .map(this::mapToStockReportDTO)

                .collect(Collectors.toList());

    }
 
    /**

     * Generate Inventory Transaction Report

     */

    public List<InventoryTransactionReportDTO> getTransactionReport() {

        List<InventoryTransaction> transactions = transactionRepository.findAll();
 
        return transactions.stream()

                .map(this::mapToTransactionReportDTO)

                .collect(Collectors.toList());

    }
 
    /**

     * Summary endpoint

     */

    public List<InventoryTransactionReportDTO> getTransactionReport(InventoryReportFilterDTO filter) {

        List<InventoryTransaction> transactions = transactionRepository.findAll();
 
        return transactions.stream()

                .filter(t -> filter.getCategoryId() == null || 

                             (t.getProduct() != null && t.getProduct().getCategory() != null &&

                              t.getProduct().getCategory().getCategoriesId().equals(filter.getCategoryId())))

                .filter(t -> {

                    if (filter.getStartDate() == null && filter.getEndDate() == null) return true;

                    LocalDateTime txDate = t.getItTransactionDate() instanceof java.sql.Date ?

                            ((java.sql.Date) t.getItTransactionDate()).toLocalDate().atStartOfDay() :

                            LocalDateTime.ofInstant(t.getItTransactionDate().toInstant(), ZoneId.systemDefault());
 
                    boolean afterStart = filter.getStartDate() == null || !txDate.isBefore(filter.getStartDate().atStartOfDay());

                    boolean beforeEnd = filter.getEndDate() == null || !txDate.isAfter(filter.getEndDate().atTime(LocalTime.MAX));

                    return afterStart && beforeEnd;

                })

                .filter(t -> filter.getTransactionType() == null || filter.getTransactionType().isEmpty() ||

                t.getItTransactionType().equalsIgnoreCase(filter.getTransactionType()))

                .map(this::mapToTransactionReportDTO)

                .collect(Collectors.toList());

    }
 
 
    /** Mapping helpers */

    private StockReportDTO mapToStockReportDTO(Product p) {

        StockReportDTO dto = new StockReportDTO();

        dto.setProductId(p.getProductsId());

        dto.setProductName(p.getProductsName());

        dto.setProductDescription(p.getProductsDescription());

        dto.setCategoryName(p.getCategory() != null ? p.getCategory().getCategoryName() : null);

        dto.setUnitPrice(p.getProductsUnitPrice());

        dto.setQuantity(p.getProductsQuantity());

        dto.setMinThreshold(p.getMinStockThreshold());

        dto.setMaxThreshold(p.getMaxStockThreshold());

        //dto.setLastUpdated(p.getLastUpdated());

        return dto;

    }
 
    private InventoryTransactionReportDTO mapToTransactionReportDTO(InventoryTransaction t) {

        InventoryTransactionReportDTO dto = new InventoryTransactionReportDTO();

        dto.setTransactionId(t.getItId());

        dto.setProductId(t.getProduct() != null ? t.getProduct().getProductsId() : null);

        dto.setProductName(t.getProduct() != null ? t.getProduct().getProductsName() : null);

        dto.setQuantity(t.getItQuantity());

        dto.setTransactionType(t.getItTransactionType());
 
        if (t.getItTransactionDate() != null) {

            if (t.getItTransactionDate() instanceof java.sql.Date) {

                // java.sql.Date → LocalDateTime

                java.sql.Date sqlDate = (java.sql.Date) t.getItTransactionDate();

                dto.setTransactionDate(

                    sqlDate.toLocalDate().atStartOfDay() // safe conversion

                );

            } else {

                // java.util.Date → LocalDateTime

                dto.setTransactionDate(

                    LocalDateTime.ofInstant(t.getItTransactionDate().toInstant(), ZoneId.systemDefault())

                );

            }

        } else {

            dto.setTransactionDate(null);

        }
 
        return dto;

    }
 
 
    public List<ProductionReportDTO> getProductionReport(ProductionReportFilterDTO filter) {

        List<ProductionSchedule> schedules = productionScheduleRepository.findByFilters(

                filter.getCategoryId(),

                filter.getStatus(),

                filter.getStartDate(),

                filter.getEndDate()

        );
 
        return schedules.stream()

                .map(this::mapToProductionReportDTO)

                .collect(Collectors.toList());

    }
 
 
    private ProductionReportDTO mapToProductionReportDTO(ProductionSchedule ps) {

        ProductionReportDTO dto = new ProductionReportDTO();

        dto.setProductionId(ps.getPsId());

        dto.setProductName(ps.getProduct() != null ? ps.getProduct().getProductsName() : null);

        dto.setCategoryName(

            ps.getProduct() != null && ps.getProduct().getCategory() != null 

                ? ps.getProduct().getCategory().getCategoryName() 

                : null

        );

        dto.setStartDate(ps.getPsStartDate() != null ? ((java.sql.Date) ps.getPsStartDate()).toLocalDate() : null);

        dto.setEndDate(ps.getPsEndDate() != null ? ((java.sql.Date) ps.getPsEndDate()).toLocalDate() : null);
 
 
        // Assuming PSQUANTITY = planned quantity

        dto.setQuantityPlanned(ps.getPsQuantity() != null ? ps.getPsQuantity() : 0);
 
        // You don’t have completed quantity in ProductionSchedule table → set default

        dto.setQuantityCompleted("COMPLETED".equalsIgnoreCase(ps.getPsStatus()) ? ps.getPsQuantity()  : 0);
 
        dto.setStatus(ps.getPsStatus());

        return dto;

    }
 
 
 
 
}
 
 
 