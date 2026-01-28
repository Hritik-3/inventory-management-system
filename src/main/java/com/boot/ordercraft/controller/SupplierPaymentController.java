package com.boot.ordercraft.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.boot.ordercraft.dto.SupplierPaymentDTO;
import com.boot.ordercraft.model.SupplierPayment;
import com.boot.ordercraft.service.SupplierPaymentService;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "http://localhost:4200")
public class SupplierPaymentController {

    @Autowired
    private SupplierPaymentService paymentService;

    // ✅ Add new payment
    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addPayment(@RequestBody SupplierPayment payment) {
        Map<String, Object> response = new LinkedHashMap<>();

        try {
            Object result = paymentService.savePayment(payment);

            // ✅ CASE 1: When auto payment (PO-based)
            if (result instanceof Map<?, ?> mapResult) {
                response.put("success", true);
                response.put("message", "Payment processed successfully for Purchase Order ID: " + payment.getPoId());
                response.put("data", mapResult);
                return ResponseEntity.ok(response);
            }

            // ✅ CASE 2: When manual payment (supplier-based)
            if (result instanceof SupplierPayment sp) {
                response.put("success", true);
                response.put("message", "Manual payment added successfully.");
                response.put("payment", sp);

                // If PO is linked, include its raw materials
                if (sp.getPoId() != null) {
                    List<Object> rawMaterials = paymentService.getRawMaterialsForPo(sp.getPoId());
                    response.put("rawMaterials", rawMaterials);
                } else {
                    response.put("rawMaterials", List.of());
                }

                return ResponseEntity.ok(response);
            }

            // ❗Fallback (should not occur)
            response.put("success", false);
            response.put("message", "Unexpected response type from service.");
            return ResponseEntity.status(500).body(response);

        } catch (RuntimeException ex) {
            // ⚠️ Expected domain-level exception (e.g., "Full payment already made")
            response.put("success", false);
            response.put("message", ex.getMessage());
            return ResponseEntity.badRequest().body(response);

        } catch (Exception ex) {
            // ⚠️ Unexpected technical exception
            response.put("success", false);
            response.put("message", "An unexpected error occurred: " + ex.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }




    // ✅ Get all payments with supplier details (DTO for cleaner JSON)
    @GetMapping("/all")
    public ResponseEntity<List<SupplierPaymentDTO>> getAllPayments() {
        List<SupplierPayment> payments = paymentService.getAllPayments();
        List<SupplierPaymentDTO> paymentDTOs = payments.stream()
                .map(SupplierPaymentDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(paymentDTOs);
    }

    // ✅ Get payments for a specific supplier
    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<List<SupplierPaymentDTO>> getBySupplier(@PathVariable Long supplierId) {
        List<SupplierPayment> payments = paymentService.getPaymentsBySupplier(supplierId);
        List<SupplierPaymentDTO> paymentDTOs = payments.stream()
                .map(SupplierPaymentDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(paymentDTOs);
    }
}
