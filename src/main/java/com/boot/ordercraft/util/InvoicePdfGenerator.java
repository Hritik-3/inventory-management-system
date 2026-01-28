package com.boot.ordercraft.util;
 
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.boot.ordercraft.dto.InvoiceResponseDTO;
import com.boot.ordercraft.dto.OrderItemDTO;
 
import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
 
 
 
import java.io.ByteArrayOutputStream;
 
public class InvoicePdfGenerator {
 
    public static byte[] generatePdf(InvoiceResponseDTO invoice) throws Exception {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
 
        PdfWriter.getInstance(document, out);
        document.open();
 
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
 
        Paragraph title = new Paragraph("Order Invoice", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(Chunk.NEWLINE);
 
        document.add(new Paragraph("Invoice ID: " + invoice.getOrderId()));
        document.add(new Paragraph("Customer ID:" + invoice.getCustomerId()));
        document.add(new Paragraph("Customer Name:" + invoice.getCustomername()));
        document.add(new Paragraph("Customer Contact: " + invoice.getPhoneNumber()));
        document.add(new Paragraph("Shipping Address: " + invoice.getBillingAddress()));
        document.add(Chunk.NEWLINE);
 
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setWidths(new int[]{4, 3, 2, 2});
 
        table.addCell("Product");
        table.addCell("Category");
        table.addCell("Quantity");
        table.addCell("Unit Price (₹)");
 
        for (OrderItemDTO item : invoice.getItems()) {
            table.addCell(item.getProductName());
            table.addCell(item.getCategory());
            table.addCell(String.valueOf(item.getQuantity()));
            table.addCell(String.format("₹%.2f", item.getUnitPrice()));
        }
 
        document.add(table);
        document.add(Chunk.NEWLINE);
 
        document.add(new Paragraph("Subtotal: ₹" + invoice.getSubtotal()));
        document.add(new Paragraph("Tax (18%): ₹" + invoice.getTax()));
        document.add(new Paragraph("Total: ₹" + invoice.getTotalPrice()));
 
        document.close();
        return out.toByteArray();
    }
}
 
 
 
 