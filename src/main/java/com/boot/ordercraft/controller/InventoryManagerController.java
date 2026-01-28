package com.boot.ordercraft.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.boot.ordercraft.dto.PurchaseOrderViewDTO;
import com.boot.ordercraft.service.InventoryManagerService;

@RestController
@RequestMapping("/api/purchase-orders")
public class InventoryManagerController {

    private final InventoryManagerService inventoryManagerService;

    public InventoryManagerController(InventoryManagerService inventoryManagerService) {
        this.inventoryManagerService = inventoryManagerService;
    }

    /** ✅ Returns ALL purchase orders with items, customers, and suppliers */
    @GetMapping("/with-items")
    public ResponseEntity<List<PurchaseOrderViewDTO>> getAllPurchaseOrdersWithItems() {
        return ResponseEntity.ok(inventoryManagerService.getAllOrdersWithItems());
    }
}
