package com.boot.ordercraft.controller;

import com.boot.ordercraft.service.ProductionScheduleReportService;
import com.boot.ordercraft.service.ReportService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Controller to serve Production Schedule report in PDF and CSV.
 */
@RestController
@RequestMapping("/api/reports")
public class ProductionScheduleReportController {

    private final ReportService reportService; // existing inventory PDF writer
    private final ProductionScheduleReportService productionScheduleReportService;

    @Autowired
    public ProductionScheduleReportController(ReportService reportService,
                                              ProductionScheduleReportService productionScheduleReportService) {
        this.reportService = reportService;
        this.productionScheduleReportService = productionScheduleReportService;
    }

    // Existing inventory endpoint (left as you had it)
    @GetMapping(value = "/inventory", produces = "application/pdf")
    public void generateInventoryReport(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"inventory-report.pdf\"");
        reportService.exportInventoryReport(response.getOutputStream());
    }

    // PDF endpoint for production schedule
    @GetMapping(value = "/production-schedule/pdf", produces = "application/pdf")
    public void downloadProductionSchedulePdf(HttpServletResponse response) throws IOException {
        byte[] pdfBytes = productionScheduleReportService.exportProductionSchedulePdfBytes();

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"production_schedule_report.pdf\"");
        response.setContentLength(pdfBytes.length);

        try (ServletOutputStream out = response.getOutputStream()) {
            out.write(pdfBytes);
            out.flush();
        } catch (IOException ex) {
            // If writing fails, reset and return 500
            response.reset();
            response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to write PDF to response");
        }
    }

    // CSV endpoint for production schedule
    @GetMapping(value = "/production-schedule/csv", produces = "text/csv")
    public void downloadProductionScheduleCsv(HttpServletResponse response) throws IOException {
        byte[] csvBytes = productionScheduleReportService.exportProductionScheduleCsvBytes();

        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"production_schedule_report.csv\"");
        response.setContentLength(csvBytes.length);

        try (ServletOutputStream out = response.getOutputStream()) {
            out.write(csvBytes);
            out.flush();
        } catch (IOException ex) {
            response.reset();
            response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to write CSV to response");
        }
    }
}
