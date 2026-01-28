package com.boot.ordercraft.controller;

import com.boot.ordercraft.dto.ProductionScheduleCreateRequest;
import com.boot.ordercraft.dto.ProductionScheduleResponse;
import com.boot.ordercraft.dto.BackendScheduleDTO;
import com.boot.ordercraft.dto.DispatchResponseDTO;
import com.boot.ordercraft.dto.ProductDTO;
import com.boot.ordercraft.dto.ProductionScheduleCompleteRequest;
//import com.boot.ordercraft.dto.ProductIdNameDTO;
import com.boot.ordercraft.model.InventoryTransaction;
import com.boot.ordercraft.model.Product;
import com.boot.ordercraft.model.ProductionSchedule;
import com.boot.ordercraft.service.FetchProductionScheduleService;
import com.boot.ordercraft.service.ProductionScheduleService;
import com.boot.ordercraft.service.ScheduleService;
import com.boot.ordercraft.service.MailService.MailService;
import com.boot.ordercraft.repository.ProductionScheduleRepository;
import com.boot.ordercraft.repository.ProductsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/production")
public class ProductionScheduleController {
	
	private final FetchProductionScheduleService fetchService;

    private final ProductionScheduleService scheduleService;
    private final ProductsRepository productRepo;
    @Autowired
    private ProductionScheduleRepository productionScheduleRepo;
    
    @Autowired
    private MailService mailService;
    
    @Autowired
    ScheduleService productionScheduleservice;

    public ProductionScheduleController(ProductionScheduleService scheduleService,
                                        ProductsRepository productRepo, FetchProductionScheduleService fetchService) {
        this.scheduleService = scheduleService;
        this.productRepo = productRepo;
        
        this.fetchService = fetchService;
    }

    // existing endpoints unchanged...
    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody ProductionScheduleCreateRequest request) {
        try {
            Object response = scheduleService.createProductionOrder(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex) {
            ProductionScheduleResponse errorResponse = new ProductionScheduleResponse();
            errorResponse.setScheduleId(null);
            errorResponse.setProductName(ex.getMessage());
            errorResponse.setQuantity(request.getQuantity());
            errorResponse.setStatus("FAILED");
            errorResponse.setProductionLineName(null);

            return ResponseEntity.status(400).body(errorResponse);
        }
    }

    @PutMapping("/{scheduleId}/complete")
    public ResponseEntity<String> completeOrder(@PathVariable Long scheduleId,
                                                @RequestBody ProductionScheduleCompleteRequest body) {
        try {
            scheduleService.completeProductionOrder(scheduleId, body.getPerformedBy());
            return ResponseEntity.ok("✅ Production order completed successfully.");
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body("❌ Error: " + ex.getMessage());
        }
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<Product>> getLowStockProducts() {
        return ResponseEntity.ok(scheduleService.getLowStockProducts());
    }

    @GetMapping("/low-stock-alert")
    public List<Product> getLowStockProductsAndSendAlert() {
        List<Product> lowStockProducts = productRepo.findLowStockProducts();

        if (!lowStockProducts.isEmpty()) {
            StringBuilder sb = new StringBuilder("⚠ Low Stock Alert:\n\n");
            for (Product product : lowStockProducts) {
                sb.append("Product: ").append(product.getProductsName())
                  .append(" | Qty: ").append(product.getProductsQuantity())
                  .append(" | Min: ").append(product.getMinStockThreshold())
                  .append("\n");
            }
            mailService.sendSimpleMessage("admin@ordercraft.com", "Low Stock Alert", sb.toString());
            System.out.println("✅ Low stock email sent!");
        }

        return lowStockProducts;
    }

    @PostMapping("/auto-schedule")
    public ResponseEntity<String> autoScheduleLowStock(@RequestParam(defaultValue = "System") String performedBy) {
        scheduleService.handleLowStockAutoSchedule(performedBy);
        return ResponseEntity.ok("Checked and scheduled production for low-stock products.");
    }
    
    @PostMapping("/dispatch/{purchaseOrderId}")
    public ResponseEntity<DispatchResponseDTO> dispatchOrder(@PathVariable Long purchaseOrderId) {
        DispatchResponseDTO response = productionScheduleservice.dispatchWholeOrder(purchaseOrderId);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/complete-all")
    public ResponseEntity<String> completeAllPlannedOrders(@RequestParam String performedBy) {
    	System.out.println("✅ performedBy received: " + performedBy);
    	scheduleService.completeAllPlannedOrders(performedBy);
        return ResponseEntity.ok("✅ All planned production orders have been completed.");
    }
    
    @GetMapping("/low-stock-status")
    public ResponseEntity<List<ProductDTO>> getLowStockProductsWithStatus() {
        return ResponseEntity.ok(scheduleService.getLowStockProductsWithScheduleStatus());
    }

    @GetMapping("/schedules")
    public List<BackendScheduleDTO> getAllSchedules() {
        List<ProductionSchedule> schedules = scheduleService.getAllSchedules();

        return schedules.stream().map(s -> {
            String startDateStr = s.getPsStartDate() != null ? s.getPsStartDate().toString() : null;
            String endDateStr = s.getPsEndDate() != null ? s.getPsEndDate().toString() : null;

            return new BackendScheduleDTO(
                s.getPsId(),
                s.getPsQuantity(),
                s.getPsStatus(),
                startDateStr,
                endDateStr,
                s.getActions(),
                s.getProduct().getProductsId(),
                s.getProduct().getProductsName(),
                s.getProductionLine() != null ? s.getProductionLine().getLineId() : null,
                s.getProductionLine() != null ? s.getProductionLine().getLineName() : null
            );
        }).collect(Collectors.toList());
    }

    /**
     * NEW: Return only product id + product name for schedule dropdown/search UI.
     * Path: GET /api/production/products-for-schedule
     */
//    @GetMapping("/products-for-schedule")
//    public ResponseEntity<List<ProductIdNameDTO>> getProductsForSchedule() {
//        List<Product> products = productRepo.findAll(); // or create a repo method to fetch minimal fields
//        List<ProductIdNameDTO> dtos = products.stream()
//                .map(p -> new ProductIdNameDTO(p.getProductsId(), p.getProductsName()))
//                .collect(Collectors.toList());
//        return ResponseEntity.ok(dtos);
//    }

    // ... other endpoints unchanged ...
}
