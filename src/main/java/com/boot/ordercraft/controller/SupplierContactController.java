package com.boot.ordercraft.controller;

import com.boot.ordercraft.model.Supplier;
import com.boot.ordercraft.service.SupplierContactService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/suppliers")
public class SupplierContactController {

    private final SupplierContactService supplierService;

    public SupplierContactController(SupplierContactService supplierService) {
        this.supplierService = supplierService;
    }

    // 🔹 Fetch all suppliers for contact management
    @GetMapping("/contacts")
    public ResponseEntity<List<Supplier>> getAllSupplierContacts() {
        List<Supplier> suppliers = supplierService.getAllSuppliers();
        return ResponseEntity.ok(suppliers);
    }

    // 🔹 Update supplier contact info with proper error messages
    @PutMapping("/{id}/contact")
    public ResponseEntity<?> updateSupplierContact(
            @PathVariable Long id,
            @RequestBody Map<String, String> contactInfo) {

        try {
            Supplier updated = supplierService.updateSupplierContact(
                    id,
                    contactInfo.get("contactPerson"),
                    contactInfo.get("email"),
                    contactInfo.get("phone")
            );

            return ResponseEntity.ok(Map.of(
                    "message", "Supplier contact info updated successfully",
                    "supplier", updated
            ));
        } catch (RuntimeException e) {
            // Return 400 Bad Request with error message
            return ResponseEntity.badRequest().body(Map.of(
                    "error", e.getMessage()
            ));
        }
    }

    // 🔹 Delete supplier contact info
    @DeleteMapping("/{id}/contact")
    public ResponseEntity<String> deleteSupplierContact(@PathVariable Long id) {
        supplierService.deleteSupplierContact(id);
        return ResponseEntity.ok("Supplier contact info deleted successfully");
    }
}
