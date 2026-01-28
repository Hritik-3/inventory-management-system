package com.boot.ordercraft.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.lowagie.text.pdf.draw.LineSeparator;

import com.boot.ordercraft.model.Supplier;
import com.boot.ordercraft.model.SupplierRating;
import com.boot.ordercraft.repository.SuppliersRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class SupplierPdfService {

    @Autowired
    private SuppliersRepository supplierRepository;

    @Autowired
    private SupplierRatingService supplierRatingService;

    private final Font titleFont  = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, Color.BLACK);
    private final Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13, Color.WHITE);
    private final Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Color.DARK_GRAY);

    // ========== SINGLE SUPPLIER ==========

    public byte[] generateSupplierPdf(Long id) throws Exception {

        Supplier s = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        // latest rating for this supplier (may be null)
        SupplierRating latestRating = supplierRatingService.getLatestRatingBySupplierId(id);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 36, 36, 60, 36);

        PdfWriter.getInstance(document, output);
        document.open();

        addHeader(document);

        Paragraph title = new Paragraph("Supplier Report", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        document.add(new LineSeparator());
        document.add(new Paragraph("\n"));

        PdfPTable table = new PdfPTable(2);
        table.setWidths(new float[]{2f, 5f});
        table.setSpacingBefore(10);
        table.setWidthPercentage(100);

        // basic details
        addRow(table, "ID",     String.valueOf(s.getSuppliersId()));
        addRow(table, "Name",   safe(s.getSuppliersName()));
        addRow(table, "Phone",  safe(s.getSuppliersPhone()));
        addRow(table, "Email",  safe(s.getSuppliersEmail()));
        addRow(table, "Contact Person", safe(s.getSuppliersContactPerson()));
        addRow(table, "Address", safe(s.getAddress()));

        // rating details (latest)
        String ratingValue   = (latestRating != null) ? String.valueOf(latestRating.getRatingValue()) : "N/A";
        String ratingDate    = (latestRating != null && latestRating.getRatingDate() != null)
                ? latestRating.getRatingDate().toString() : "N/A";
        String ratingComment = (latestRating != null) ? safe(latestRating.getComments()) : "N/A";

        addRow(table, "Latest Rating", ratingValue);
        addRow(table, "Rating Date",   ratingDate);
        addRow(table, "Rating Comment", ratingComment);

        document.add(table);

        document.close();
        return output.toByteArray();
    }

    // ========== ALL SUPPLIERS ==========

    public byte[] generateAllSuppliersPdf() throws Exception {

        List<Supplier> list = supplierRepository.findAll();

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 36, 36, 60, 36);

        PdfWriter.getInstance(document, output);
        document.open();

        addHeader(document);

        Paragraph title = new Paragraph("All Suppliers Report", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        document.add(new LineSeparator());
        document.add(new Paragraph("\n"));

        // ID | Name | Phone | Email | Latest Rating
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);

        addTableHeader(table, "ID", "Name", "Phone", "Email", "Latest Rating");

        boolean alternate = false;

        for (Supplier s : list) {
            Color bg = alternate ? new Color(245, 245, 245) : Color.WHITE;
            alternate = !alternate;

            SupplierRating latest = supplierRatingService
                    .getLatestRatingBySupplierId(s.getSuppliersId());

            String ratingVal = (latest != null)
                    ? String.valueOf(latest.getRatingValue())
                    : "N/A";

            addTableCell(table, String.valueOf(s.getSuppliersId()), bg);
            addTableCell(table, safe(s.getSuppliersName()), bg);
            addTableCell(table, safe(s.getSuppliersPhone()), bg);
            addTableCell(table, safe(s.getSuppliersEmail()), bg);
            addTableCell(table, ratingVal, bg);
        }

        document.add(table);
        document.close();
        return output.toByteArray();
    }

    // ========== Helper methods ==========

    private void addHeader(Document document) throws DocumentException {
        Paragraph company = new Paragraph("Supplier Management System",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16));
        company.setAlignment(Element.ALIGN_CENTER);
        document.add(company);

        Paragraph sub = new Paragraph("Generated Report",
                FontFactory.getFont(FontFactory.HELVETICA, 11));
        sub.setAlignment(Element.ALIGN_CENTER);
        document.add(sub);

        document.add(new Paragraph("\n"));
    }

    private void addRow(PdfPTable table, String label, String value) {
        PdfPCell cell1 = new PdfPCell(new Phrase(label, headerFont));
        cell1.setBackgroundColor(new Color(40, 53, 147));
        cell1.setPadding(8);

        PdfPCell cell2 = new PdfPCell(new Phrase(value, normalFont));
        cell2.setPadding(8);

        table.addCell(cell1);
        table.addCell(cell2);
    }

    private void addTableHeader(PdfPTable table, String... headers) {
        for (String h : headers) {
            PdfPCell header = new PdfPCell(new Phrase(h, headerFont));
            header.setBackgroundColor(new Color(40, 53, 147));
            header.setHorizontalAlignment(Element.ALIGN_CENTER);
            header.setPadding(8);
            table.addCell(header);
        }
    }

    private void addTableCell(PdfPTable table, String text, Color bg) {
        PdfPCell cell = new PdfPCell(new Phrase(text, normalFont));
        cell.setBackgroundColor(bg);
        cell.setPadding(8);
        table.addCell(cell);
    }

    private String safe(String val) {
        return (val == null || val.isBlank()) ? "N/A" : val;
    }
}
