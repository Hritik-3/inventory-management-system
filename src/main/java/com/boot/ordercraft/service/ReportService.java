package com.boot.ordercraft.service;

import com.boot.ordercraft.model.InventoryTransaction;
import  com.boot.ordercraft.repository.InventoryTransactionRepository;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.util.List;

@Service
public class ReportService {

    @Autowired
    private InventoryTransactionRepository transactionRepo;

    public void exportInventoryReport(OutputStream out) {
        Document document = new Document();
        try {
            // Create PDF writer
            PdfWriter.getInstance(document, out);

            // Open the document
            document.open();

            // Title
            document.add(new Paragraph("📦 Inventory Transactions Report"));
            document.add(new Paragraph(" ")); // empty line

            // Table with 6 columns
            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);

            // Table headers
            table.addCell("ID");
            table.addCell("Product");
            table.addCell("Performed By");
            table.addCell("Date");
            table.addCell("Type");
            table.addCell("Quantity");

            // Fetch transactions and fill table
            List<InventoryTransaction> txns = transactionRepo.findAll();
            for (InventoryTransaction txn : txns) {
                table.addCell(txn.getItId().toString());
                table.addCell(txn.getProduct().getProductsName());
                table.addCell(txn.getItPerformedBy());
                table.addCell(txn.getItTransactionDate().toString());
                table.addCell(txn.getItTransactionType());
                table.addCell(txn.getItQuantity().toString());
            }

            // Add table to document
            document.add(table);

        } catch (DocumentException e) {
            throw new RuntimeException("Error generating PDF report", e);
        } finally {
            document.close();
        }
    }
}
