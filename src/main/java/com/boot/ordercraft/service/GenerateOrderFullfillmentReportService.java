package com.boot.ordercraft.service;

import com.boot.ordercraft.model.PurchaseOrder;

import com.boot.ordercraft.model.PurchaseOrderItem;

import com.boot.ordercraft.repository.PurchaseOrdersRepository;

import com.lowagie.text.*;

import com.lowagie.text.pdf.PdfPCell;

import com.lowagie.text.pdf.PdfPTable;

import com.lowagie.text.pdf.PdfWriter;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.awt.Color;

import java.io.ByteArrayOutputStream;

@Service

public class GenerateOrderFullfillmentReportService {

    @Autowired

    private PurchaseOrdersRepository purchaseOrdersRepository;

    public byte[] generateOrderFullfillmentReport(Long poId) throws Exception {

        PurchaseOrder po = purchaseOrdersRepository.findById(poId)

                .orElseThrow(() -> new RuntimeException("Purchase Order Not Found"));

        Document document = new Document(PageSize.A4);

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, out);

        document.open();

        // ---------- TITLE ----------

        Font titleFont = new Font(Font.HELVETICA, 20, Font.BOLD);

        Paragraph title = new Paragraph("Order Fulfillment Report", titleFont);

        title.setAlignment(Element.ALIGN_CENTER);

        document.add(title);

        document.add(new Paragraph(" "));

        Font subFont = new Font(Font.HELVETICA, 12);

        // ---------- ORDER DETAILS ----------

        document.add(new Paragraph("Purchase Order ID: " + po.getPoId(), subFont));

        document.add(new Paragraph("Order Date: " + po.getPoOrderDate(), subFont));

        document.add(new Paragraph("Expected Delivery: " + po.getPoExpectedDelivery_date(), subFont));

        document.add(new Paragraph("Delivery Status: " + po.getPoDeliveryStatus(), subFont));

        document.add(new Paragraph("Order Type: " + po.getPoOrderType(), subFont));

        document.add(new Paragraph(" "));

        // ---------- SUPPLIER DETAILS ----------

        if (po.getSupplier() != null) {

            document.add(new Paragraph("Supplier Name: " + po.getSupplier().getSuppliersName(), subFont));

            document.add(new Paragraph("Supplier Email: " + po.getSupplier().getSuppliersEmail(), subFont));

            document.add(new Paragraph("Supplier Phone: " + po.getSupplier().getSuppliersPhone(), subFont));

            document.add(new Paragraph(" "));    

        }

        // ---------- CUSTOMER DETAILS (if any) ----------

        if (po.getCustomer() != null) {

            document.add(new Paragraph("Customer: " + po.getCustomer().getName(), subFont));

            document.add(new Paragraph("Customer Phone: " + po.getCustomer().getPhone(), subFont));

            document.add(new Paragraph("Customer Email: " + po.getCustomer().getEmail(), subFont));

            document.add(new Paragraph(" "));

        }

        document.add(new Paragraph("----------------------------------------------------"));

        document.add(new Paragraph(" "));

        // ---------- TABLE ----------

        PdfPTable table = new PdfPTable(6);

        table.setWidthPercentage(100);

        addHeader(table, "Item Name");

        addHeader(table, "Type");

        addHeader(table, "Quantity");

        addHeader(table, "Cost");

        addHeader(table, "Returned?");

        addHeader(table, "Total");

        for (PurchaseOrderItem item : po.getItems()) {

            String itemName = "";

            if (item.getProduct() != null) {

                itemName = item.getProduct().getProductsName();

            } else if (item.getRawmaterial() != null) {

                itemName = item.getRawmaterial().getRwName();

            }

            String type = (item.getProduct() != null) ? "PRODUCT" : "RAW MATERIAL";

            table.addCell(itemName);

            table.addCell(type);

            table.addCell(String.valueOf(item.getPoiQuantity()));

            table.addCell(String.valueOf(item.getPoiCost()));

            table.addCell(item.isReturned() ? "Yes" : "No");

            Float total = item.getPoiCost() * item.getPoiQuantity();

            table.addCell(String.valueOf(total));

        }

        document.add(table);

        document.add(new Paragraph(" "));

        document.close();

        return out.toByteArray();

    }

    private void addHeader(PdfPTable table, String title) {

        PdfPCell header = new PdfPCell();

        header.setBackgroundColor(Color.LIGHT_GRAY);

        header.setPhrase(new Phrase(title, new Font(Font.HELVETICA, 12, Font.BOLD)));

        table.addCell(header);

    }

}
 