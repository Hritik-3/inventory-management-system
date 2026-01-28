package com.boot.ordercraft.controller;

import com.boot.ordercraft.service.GenerateOrderFullfillmentReportService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpHeaders;

import org.springframework.http.MediaType;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController

@RequestMapping("/api/reports")

public class OrderFullfillmentReportController {

    @Autowired

    private GenerateOrderFullfillmentReportService reportService;

    @GetMapping("/purchase-order/{poId}")

    public ResponseEntity<byte[]> generate(@PathVariable Long poId) {

        try {

            byte[] pdf = reportService.generateOrderFullfillmentReport(poId);

            return ResponseEntity.ok()

                    .header(HttpHeaders.CONTENT_DISPOSITION,

                            "attachment; filename=OrderFullfillmentReport-" + poId + ".pdf")

                    .contentType(MediaType.APPLICATION_PDF)

                    .body(pdf);

        } catch (Exception e) {

            return ResponseEntity.status(500).body(null);

        }

    }

}
 