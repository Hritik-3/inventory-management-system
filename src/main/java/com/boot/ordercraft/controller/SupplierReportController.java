package com.boot.ordercraft.controller;

import com.boot.ordercraft.service.SupplierPdfService;
import com.boot.ordercraft.service.SupplierCsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/suppliers/report")
@CrossOrigin(origins = "http://localhost:4200")
public class SupplierReportController {

    @Autowired
    private SupplierPdfService supplierPdfService;

    @Autowired
    private SupplierCsvService supplierCsvService;

    // ------------------------------------
    // PDF
    // ------------------------------------
    // http://localhost:8086/api/suppliers/report/pdf
    // http://localhost:8086/api/suppliers/report/pdf?id=2
    @GetMapping(value = "/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> viewSupplierPdf(
            @RequestParam(required = false) Long id) throws Exception {

        byte[] pdf;

        if (id == null) {
            pdf = supplierPdfService.generateAllSuppliersPdf();
        } else {
            pdf = supplierPdfService.generateSupplierPdf(id);
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=supplier_report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    // ------------------------------------
    // CSV
    // ------------------------------------
    // http://localhost:8086/api/suppliers/report/csv
    // http://localhost:8086/api/suppliers/report/csv?id=2
    @GetMapping(value = "/csv", produces = "text/csv")
    public ResponseEntity<byte[]> viewSupplierCsv(
            @RequestParam(required = false) Long id) {

        byte[] csv;

        if (id == null) {
            csv = supplierCsvService.generateAllSuppliersCsv();
        } else {
            csv = supplierCsvService.generateSupplierCsv(id);
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=supplier_report.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csv);
    }
}
