package com.boot.ordercraft.util;
 
import com.boot.ordercraft.dto.StockReportDTO;
import com.boot.ordercraft.dto.InventoryTransactionReportDTO;
import com.boot.ordercraft.dto.ProductionReportDTO;
 
import org.xhtmlrenderer.pdf.ITextRenderer;
 
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
 
public class InventoryReportExportUtil {
 
	private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
 
 
    /** ===== Stock Level Export (PDF) ===== */
    public static byte[] exportStockReportPdf(List<StockReportDTO> report) throws Exception {
        String html = buildStockReportHtml(report);
 
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(html);
            renderer.layout();
            renderer.createPDF(outputStream);
            return outputStream.toByteArray();
        }
    }
 
 
    private static String csvSafe(Object value) {
        if (value == null) return "\"\"";
        String text = value.toString().replace("\"", "\"\"");
        return "\"" + text + "\"";
    }
 
 
    private static String buildStockReportHtml(List<StockReportDTO> report) throws Exception {
        String logoUrl = encodeLogo();
 
        StringBuilder rows = new StringBuilder();
        int index = 1;
        for (StockReportDTO dto : report) {
            rows.append("<tr>")
                .append("<td>").append(index++).append("</td>")
                .append("<td>").append(dto.getProductName() != null ? dto.getProductName() : "").append("</td>")
                .append("<td>").append(dto.getProductDescription() != null ? dto.getProductDescription() : "").append("</td>")
                .append("<td>").append(dto.getCategoryName() != null ? dto.getCategoryName() : "").append("</td>")
                .append("<td>").append(dto.getUnitPrice() != null ? String.format("%.2f", dto.getUnitPrice()) : "").append("</td>")
                .append("<td>").append(dto.getQuantity() != null ? dto.getQuantity() : "").append("</td>")
                .append("<td>").append(dto.getMinThreshold() != null ? dto.getMinThreshold() : "").append("</td>")
                .append("<td>").append(dto.getMaxThreshold() != null ? dto.getMaxThreshold() : "").append("</td>")
                //.append("<td>").append(dto.getLastUpdated() != null ? dto.getLastUpdated().format(dtf) : "").append("</td>")
                .append("</tr>");
        }
 
        return baseHtml("Stock Level Report", logoUrl,
                "<tr><th>#</th><th>Name</th><th>Description</th><th>Category</th><th>Unit Price</th><th>Quantity</th><th>Min Threshold</th><th>Max Threshold</th></tr>",
                rows.toString());
    }

 

    /** ===== Stock Level Export (CSV) ===== */
    public static byte[] exportStockReportCsv(List<StockReportDTO> report) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(baos);
 
        writer.write("ID,Name,Description,Category,Unit Price,Quantity,Min Threshold,Max Threshold\n");
 
        for (StockReportDTO dto : report) {
            writer.write(String.format("%d,%s,%s,%s,%.2f,%d,%d,%d\n",
                    dto.getProductId(),
                    dto.getProductName().replaceAll(",", " "),
                    dto.getProductDescription() != null ? dto.getProductDescription().replaceAll(",", " ") : "",
                    dto.getCategoryName() != null ? dto.getCategoryName().replaceAll(",", " ") : "",
                    dto.getUnitPrice(),
                    dto.getQuantity(),
                    dto.getMinThreshold(),
                    dto.getMaxThreshold()
 
            ));
        }
 
        writer.flush();
        writer.close();
        return baos.toByteArray();
    }
 
 
    /** ===== Transaction Export (PDF) ===== */
    public static byte[] exportTransactionReportPdf(List<InventoryTransactionReportDTO> report) throws Exception {
        String html = buildTransactionReportHtml(report);
 
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(html);
            renderer.layout();
            renderer.createPDF(outputStream);
            return outputStream.toByteArray();
        }
    }
 
    private static String buildTransactionReportHtml(List<InventoryTransactionReportDTO> report) throws Exception {
        String logoUrl = encodeLogo();
 
        StringBuilder rows = new StringBuilder();
        int index = 1;
        for (InventoryTransactionReportDTO dto : report) {
            rows.append("<tr>")
                .append("<td>").append(index++).append("</td>")
                .append("<td>").append(dto.getProductName() != null ? dto.getProductName() : "").append("</td>")
                .append("<td>").append(dto.getProductId() != null ? dto.getProductId() : "").append("</td>")
                .append("<td>").append(dto.getQuantity() != null ? dto.getQuantity() : "").append("</td>")
                .append("<td>").append(dto.getTransactionType() != null ? dto.getTransactionType() : "").append("</td>")
                .append("<td>").append(dto.getTransactionDate() != null ? dto.getTransactionDate().format(dtf) : "").append("</td>")
                .append("</tr>");
        }
 
        String headers =
            "<tr><th>#</th><th>Product Name</th><th>Product ID</th><th>Quantity</th><th>Type</th><th>Date</th></tr>";
 
        // ✅ Add colgroup with fixed widths
        String colgroup =
        	    "<colgroup>" +
        	        "<col style='width:5%'/>" +   // #
        	        "<col style='width:25%'/>" +  // Product Name
        	        "<col style='width:15%'/>" +  // Product ID
        	        "<col style='width:10%'/>" +  // Quantity
        	        "<col style='width:15%'/>" +  // Type
        	        "<col style='width:30%; white-space:nowrap; text-align:center;'/>" +  // Date
        	    "</colgroup>";
 
 
 
        return baseHtml("Inventory Transaction Report", logoUrl,
                colgroup + headers, rows.toString());
    }
 
/** ===== Transaction Export (CSV) ===== **/
 
    public static byte[] exportTransactionReportCsv(List<InventoryTransactionReportDTO> report) throws Exception {
 
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
 
        OutputStreamWriter writer = new OutputStreamWriter(baos);

        writer.write("Transaction ID,Product Name,Product ID,Quantity,Transaction Type,Transaction Date\n");

        for (InventoryTransactionReportDTO dto : report) {
 
        	writer.write(csvSafe(dto.getTransactionId()) + "," +
 
                    csvSafe(dto.getProductName()) + "," +
 
                    csvSafe(dto.getProductId()) + "," +
 
                    csvSafe(dto.getQuantity()) + "," +
 
                    csvSafe(dto.getTransactionType()) + "," +
 
                    csvSafe(dto.getTransactionDate() != null ? dto.getTransactionDate().format(dtf) : "")
 
                    + "\n");

        }

        writer.flush();
 
        writer.close();
 
        return baos.toByteArray();
 
    }


    /** ===== Production Report (PDF) ===== */
 
    public static byte[] exportProductionReportPdf(List<ProductionReportDTO> report) throws Exception {
 
        String html = buildProductionReportHtml(report);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
 
            ITextRenderer renderer = new ITextRenderer();
 
            renderer.setDocumentFromString(html);
 
            renderer.layout();
 
            renderer.createPDF(outputStream);
 
            return outputStream.toByteArray();
 
        }
 
    }

    private static String buildProductionReportHtml(List<ProductionReportDTO> report) throws Exception {
 
        String logoUrl = encodeLogo();

        StringBuilder rows = new StringBuilder();
 
        int index = 1;
 
        for (ProductionReportDTO dto : report) {
 
            rows.append("<tr>")
 
                .append("<td>").append(index++).append("</td>")
 
                .append("<td>").append(dto.getProductName() != null ? dto.getProductName() : "").append("</td>")
 
                .append("<td>").append(dto.getCategoryName() != null ? dto.getCategoryName() : "").append("</td>")
 
                .append("<td>").append(dto.getQuantityPlanned()).append("</td>")
 
                //.append("<td>").append(dto.getQuantityCompleted()).append("</td>")
 
                .append("<td>").append(dto.getStartDate() != null ? dto.getStartDate().format(dtf) : "").append("</td>")
 
                .append("<td>").append(dto.getEndDate() != null ? dto.getEndDate().format(dtf) : "").append("</td>")
 
                .append("<td>").append(dto.getStatus() != null ? dto.getStatus() : "").append("</td>")
 
                .append("</tr>");
 
        }

        return baseHtml("Production Report", logoUrl,
 
                "<tr><th>#</th><th>Product</th><th>Category</th><th>Planned Qty</th><th>Start Date</th><th>End Date</th><th>Status</th></tr>",
 
                rows.toString());
 
    }


    /** ===== Production Report (CSV) ===== */
 
    public static byte[] exportProductionReportCsv(List<ProductionReportDTO> report) throws Exception {
 
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
 
        OutputStreamWriter writer = new OutputStreamWriter(baos);

        writer.write("Production ID,Product,Category,Planned Qty,Completed Qty,Start Date,End Date,Status\n");

        for (ProductionReportDTO dto : report) {
 
        	writer.write(csvSafe(dto.getProductionId()) + "," +
 
                    csvSafe(dto.getProductName()) + "," +
 
                    csvSafe(dto.getCategoryName()) + "," +
 
                    csvSafe(dto.getQuantityPlanned()) + "," +
 
                    csvSafe(dto.getQuantityCompleted()) + "," +
 
                    csvSafe(dto.getStartDate() != null ? dto.getStartDate().format(dtf) : "") + "," +
 
                    csvSafe(dto.getEndDate() != null ? dto.getEndDate().format(dtf) : "") + "," +
 
                    csvSafe(dto.getStatus())
 
                    + "\n");


        }

        writer.flush();
 
        writer.close();
 
        return baos.toByteArray();
 
    }


    /** ===== Shared Helpers ===== */
 
    private static String baseHtml(String title, String logoUrl, String headers, String rows) {
 
        return "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" " +
 
                "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">" +
 
                "<html xmlns=\"http://www.w3.org/1999/xhtml\"><head>" +
 
                "<meta charset='UTF-8'/>" +   // ✅ self-closed
 
                "<style>" +
 
                "body { font-family: 'Segoe UI', sans-serif; background: #f4f7fa; margin:0; padding:20px; }" +
 
                ".report-box { max-width: 1000px; margin: auto; background: #fff; padding: 30px; border-radius: 10px; box-shadow: 0 0 15px rgba(0,0,0,0.1); }" +
 
                ".header { display:flex; align-items:center; margin-bottom:20px; }" +
 
                ".logo { width:60px; margin-right:20px; }" +
 
                "h1 { color:#2d3748; margin:0; }" +
 
                "table { width:100%; border-collapse: collapse; margin-top:20px; }" +
 
                "th, td { padding: 10px; border: 1px solid #e2e8f0; font-size: 12px; }" +
 
                "th { background: #edf2f7; text-align: left; }" +
 
                "tr:nth-child(even) { background: #f9fafb; }" +
 
                ".footer { text-align:center; margin-top:30px; font-size:12px; color:#a0aec0; }" +
 
                "</style></head><body>" +
 
                "<div class='report-box'>" +
 
                "<div class='header'><img src='" + logoUrl + "' class='logo' alt='Logo'/>" +  // ✅ self-closed
 
                "<h1>" + title + "</h1></div>" +
 
                "<table><thead>" + headers + "</thead><tbody>" + rows + "</tbody></table>" +
 
                "<div class='footer'>Generated by OrderKraft Inventory System on " + java.time.LocalDateTime.now().format(dtf) + "</div>" +
 
                "</div></body></html>";
 
    }

    private static String encodeLogo() throws Exception {
 
        byte[] imageBytes = Files.readAllBytes(Paths.get("src/main/resources/orderkraft_logo.png"));
 
        String base64Logo = Base64.getEncoder().encodeToString(imageBytes);
 
        return "data:image/png;base64," + base64Logo;
 
    }
 
}
 
 