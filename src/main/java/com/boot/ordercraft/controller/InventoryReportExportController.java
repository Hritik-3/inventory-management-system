package com.boot.ordercraft.controller;
 
import com.boot.ordercraft.dto.*;

import com.boot.ordercraft.service.InventoryReportService;

import com.boot.ordercraft.util.InventoryReportExportUtil;

import org.springframework.http.HttpHeaders;

import org.springframework.http.MediaType;

import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;
 
import java.util.List;
 
@RestController

@RequestMapping("/api/reports/export")

public class InventoryReportExportController {
 
    private final InventoryReportService reportService;
 
    public InventoryReportExportController(InventoryReportService reportService) {

        this.reportService = reportService;

    }
 
    @PostMapping("/stock")

//    @PreAuthorize("hasRole('IM')")

    public ResponseEntity<byte[]> exportStockReport(

            @RequestParam(name = "exportType", defaultValue = "pdf") String type,

            @RequestBody InventoryReportFilterDTO filter) throws Exception {
 
        List<StockReportDTO> report = reportService.getStockReport(filter);
 
        byte[] data;

        String filename;
 
        if ("csv".equalsIgnoreCase(type)) {

            data = InventoryReportExportUtil.exportStockReportCsv(report);

            filename = "stock_report.csv";

        } else {

            data = InventoryReportExportUtil.exportStockReportPdf(report);

            filename = "stock_report.pdf";

        }
 
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType("csv".equalsIgnoreCase(type) ? MediaType.TEXT_PLAIN : MediaType.APPLICATION_PDF);

        headers.setContentDispositionFormData("filename", filename);

        return ResponseEntity.ok().headers(headers).body(data);

    }
 
 
    @PostMapping("/transactions")

//    @PreAuthorize("hasRole('IM')")

    public ResponseEntity<byte[]> exportTransactionReport(

            @RequestParam(name = "exportType", defaultValue = "pdf") String type,

            @RequestBody InventoryReportFilterDTO filter) throws Exception {
 
        // Make a new service method to accept filters for transactions

        List<InventoryTransactionReportDTO> report = reportService.getTransactionReport(filter);
 
        byte[] data;

        String filename;
 
        if ("csv".equalsIgnoreCase(type)) {

            data = InventoryReportExportUtil.exportTransactionReportCsv(report);

            filename = "transaction_report.csv";

        } else {

            data = InventoryReportExportUtil.exportTransactionReportPdf(report);

            filename = "transaction_report.pdf";

        }
 
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType("csv".equalsIgnoreCase(type) ? MediaType.TEXT_PLAIN : MediaType.APPLICATION_PDF);

        headers.setContentDispositionFormData("filename", filename);

        return ResponseEntity.ok().headers(headers).body(data);

    }
 
 
    @PostMapping("/production")

//    @PreAuthorize("hasRole('IM')")

    public ResponseEntity<byte[]> exportProductionReport(

            @RequestParam(name = "exportType", defaultValue = "pdf") String type,

            @RequestBody ProductionReportFilterDTO filter) throws Exception {
 
        // Call service to get production report data

        List<ProductionReportDTO> report = reportService.getProductionReport(filter);
 
        byte[] data;

        String filename;
 
        if ("csv".equalsIgnoreCase(type)) {

            data = InventoryReportExportUtil.exportProductionReportCsv(report);

            filename = "production_report.csv";

        } else {

            data = InventoryReportExportUtil.exportProductionReportPdf(report);

            filename = "production_report.pdf";

        }
 
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType("csv".equalsIgnoreCase(type) ? MediaType.TEXT_PLAIN : MediaType.APPLICATION_PDF);

        headers.setContentDispositionFormData("attachment", filename); // <-- better for download
 
        return ResponseEntity.ok().headers(headers).body(data);

    }
 
 
    @PostMapping("/preview/production")

//    @PreAuthorize("hasRole('IM')")

    public ResponseEntity<List<ProductionReportDTO>> previewProductionReport(

            @RequestBody ProductionReportFilterDTO filter) {
 
        List<ProductionReportDTO> report = reportService.getProductionReport(filter);

        return ResponseEntity.ok(report);  // <-- returns JSON list

    }
 
 
    @PostMapping("/preview/stock")

//    @PreAuthorize("hasRole('IM')")

    public ResponseEntity<List<StockReportDTO>> previewStockReport(

            @RequestBody InventoryReportFilterDTO filter) {

        List<StockReportDTO> report = reportService.getStockReport(filter);

        return ResponseEntity.ok(report);

    }
 
    @PostMapping("/preview/transactions")

//    @PreAuthorize("hasRole('IM')")

    public ResponseEntity<List<InventoryTransactionReportDTO>> previewTransactionReport(

            @RequestBody InventoryReportFilterDTO filter) {

        List<InventoryTransactionReportDTO> report = reportService.getTransactionReport(filter);

        return ResponseEntity.ok(report);

    }
 
 
}

 