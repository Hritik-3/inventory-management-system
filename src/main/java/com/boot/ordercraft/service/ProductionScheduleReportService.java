package com.boot.ordercraft.service;

import com.boot.ordercraft.model.ProductionSchedule;
import com.boot.ordercraft.repository.ProductionScheduleRepository;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Service that generates Production Schedule as PDF bytes and CSV bytes.
 */
@Service
public class ProductionScheduleReportService {

    private final ProductionScheduleRepository scheduleRepo;
    private final SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    public ProductionScheduleReportService(ProductionScheduleRepository scheduleRepo) {
        this.scheduleRepo = scheduleRepo;
    }

    /**
     * Build the production schedule PDF and return the bytes.
     */
    public byte[] exportProductionSchedulePdfBytes() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, baos);
            document.open();

            document.add(new Paragraph("Production Schedule Report"));
            document.add(new Paragraph(" ")); // spacing

            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);

            // Header row
            table.addCell("ID");
            table.addCell("Product");
            table.addCell("Start Date");
            table.addCell("End Date");
            table.addCell("Quantity");
            table.addCell("Status");

            List<ProductionSchedule> schedules = scheduleRepo.findAll();

            if (schedules == null || schedules.isEmpty()) {
                // single row showing no data
                table.addCell("-");
                table.addCell("No schedules found");
                table.addCell("-");
                table.addCell("-");
                table.addCell("-");
                table.addCell("-");
            } else {
                for (ProductionSchedule ps : schedules) {
                    String id = ps.getPsId() != null ? ps.getPsId().toString() : "N/A";
                    String productName = "N/A";
                    try {
                        if (ps.getProduct() != null && ps.getProduct().getProductsName() != null) {
                            productName = ps.getProduct().getProductsName();
                        }
                    } catch (Exception ignored) {
                        productName = "N/A";
                    }
                    String start = ps.getPsStartDate() != null ? fmt.format(ps.getPsStartDate()) : "N/A";
                    String end = ps.getPsEndDate() != null ? fmt.format(ps.getPsEndDate()) : "N/A";
                    String qty = ps.getPsQuantity() != null ? ps.getPsQuantity().toString() : "0";
                    String status = ps.getPsStatus() != null ? ps.getPsStatus() : "N/A";

                    table.addCell(id);
                    table.addCell(productName);
                    table.addCell(start);
                    table.addCell(end);
                    table.addCell(qty);
                    table.addCell(status);
                }
            }

            document.add(table);
        } catch (DocumentException ex) {
            throw new RuntimeException("Error generating Production Schedule PDF", ex);
        } finally {
            document.close();
        }

        return baos.toByteArray();
    }

    /**
     * Build the production schedule CSV and return bytes (UTF-8).
     * CSV fields are quoted and quotes inside values doubled per RFC4180.
     */
    public byte[] exportProductionScheduleCsvBytes() {
        StringBuilder sb = new StringBuilder();

        // CSV header
        String[] headers = {"ID", "Product", "Start Date", "End Date", "Quantity", "Status"};
        appendCsvRow(sb, headers);

        List<ProductionSchedule> schedules = scheduleRepo.findAll();

        if (schedules == null || schedules.isEmpty()) {
            // optional: put a single row indicating no data
            appendCsvRow(sb, new String[]{"-", "No schedules found", "-", "-", "-", "-"});
        } else {
            for (ProductionSchedule ps : schedules) {
                String id = ps.getPsId() != null ? ps.getPsId().toString() : "";
                String productName = "";
                try {
                    if (ps.getProduct() != null && ps.getProduct().getProductsName() != null) {
                        productName = ps.getProduct().getProductsName();
                    }
                } catch (Exception ignored) {
                    productName = "";
                }
                String start = ps.getPsStartDate() != null ? fmt.format(ps.getPsStartDate()) : "";
                String end = ps.getPsEndDate() != null ? fmt.format(ps.getPsEndDate()) : "";
                String qty = ps.getPsQuantity() != null ? ps.getPsQuantity().toString() : "";
                String status = ps.getPsStatus() != null ? ps.getPsStatus() : "";

                AppendableRow row = new AppendableRow(id, productName, start, end, qty, status);
                appendCsvRow(sb, row.toArray());
            }
        }

        // return bytes in UTF-8 BOM-aware manner if required by Excel; here plain UTF-8 is returned.
        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    // Helper to append a CSV row (fields already in order). Fields will be quoted and escaped.
    private void appendCsvRow(StringBuilder sb, String[] fields) {
        for (int i = 0; i < fields.length; i++) {
            sb.append(escapeCsv(fields[i]));
            if (i < fields.length - 1) sb.append(',');
        }
        sb.append('\n');
    }

    // Escape CSV field: wrap in quotes and double internal quotes. If empty, write empty quoted string.
    private String escapeCsv(String field) {
        if (field == null) field = "";
        // If field contains newline, double quote, or comma, always quote. We'll quote everything to be safe.
        String escaped = field.replace("\"", "\"\"");
        return "\"" + escaped + "\"";
    }

    // small helper record-like class to build rows (keeps code readable)
    private static class AppendableRow {
        private final String[] cols;
        AppendableRow(String... values) { this.cols = values; }
        String[] toArray() { return cols; }
    }
}
