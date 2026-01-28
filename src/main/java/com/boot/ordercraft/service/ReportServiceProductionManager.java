//package com.boot.ordercraft.service;
//
//import com.boot.ordercraft.model.ProductionSchedule;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.ss.usermodel.Row;
//import org.springframework.stereotype.Service;
//import com.lowagie.text.*;
//import com.lowagie.text.pdf.PdfPTable;
//import com.lowagie.text.pdf.PdfWriter;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.util.List;
//
//@Service
//public class ReportServiceProductionManager {
//
//    public byte[] generateExcelReport(List<ProductionSchedule> schedules) {
//        try (XSSFWorkbook workbook = new XSSFWorkbook();
//             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
//
//            XSSFSheet sheet = workbook.createSheet("Schedules");
//            int rowNum = 0;
//
//            Row header = sheet.createRow(rowNum++);
//            header.createCell(0).setCellValue("Schedule ID");
//            header.createCell(1).setCellValue("Product");
//            header.createCell(2).setCellValue("Quantity");
//            header.createCell(3).setCellValue("Status");
//            header.createCell(4).setCellValue("Line");
//            header.createCell(5).setCellValue("Start Date");
//            header.createCell(6).setCellValue("End Date");
//
//            for (ProductionSchedule s : schedules) {
//                Row row = sheet.createRow(rowNum++);
//                row.createCell(0).setCellValue(s.getPsId());
//                row.createCell(1).setCellValue(s.getProduct().getProductsName());
//                row.createCell(2).setCellValue(s.getPsQuantity());
//                row.createCell(3).setCellValue(s.getPsStatus());
//                row.createCell(4).setCellValue(s.getProductionLine() != null ? s.getProductionLine().getLineName() : "-");
//                row.createCell(5).setCellValue(s.getPsStartDate() != null ? s.getPsStartDate().toString() : "");
//                row.createCell(6).setCellValue(s.getPsEndDate() != null ? s.getPsEndDate().toString() : "");
//            }
//
//            workbook.write(bos);
//            return bos.toByteArray();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            return new byte[0]; // return empty array on error
//        }
//    }
//
//    public byte[] generatePdfReport(List<ProductionSchedule> schedules) {
//        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
//            com.itextpdf.text.Document document = new com.itextpdf.text.Document();
//            com.itextpdf.text.pdf.PdfWriter.getInstance(document, bos);
//
//            document.open();
//            document.add(new com.itextpdf.text.Paragraph("Production Schedule Report\n\n"));
//
//            for (ProductionSchedule s : schedules) {
//                document.add(new com.itextpdf.text.Paragraph(
//                        "ID: " + s.getPsId() +
//                        ", Product: " + s.getProduct().getProductsName() +
//                        ", Qty: " + s.getPsQuantity() +
//                        ", Status: " + s.getPsStatus() +
//                        ", Line: " + (s.getProductionLine() != null ? s.getProductionLine().getLineName() : "-") +
//                        ", Start: " + s.getPsStartDate() +
//                        ", End: " + s.getPsEndDate()
//                ));
//            }
//
//            document.close();
//            return bos.toByteArray();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new byte[0];
//        }
//    }
//}
