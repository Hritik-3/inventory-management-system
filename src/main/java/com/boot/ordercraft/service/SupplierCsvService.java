package com.boot.ordercraft.service;

import com.boot.ordercraft.model.Supplier;
import com.boot.ordercraft.model.SupplierRating;
import com.boot.ordercraft.repository.SuppliersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class SupplierCsvService {

    @Autowired
    private SuppliersRepository supplierRepository;

    @Autowired
    private SupplierRatingService supplierRatingService;

    // ========= SINGLE SUPPLIER =========

    public byte[] generateSupplierCsv(Long id) {

        Supplier s = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found with id " + id));

        SupplierRating latest = supplierRatingService.getLatestRatingBySupplierId(id);

        StringBuilder sb = new StringBuilder();

        // header
        sb.append("ID,Name,Phone,Email,Contact Person,Address,Latest Rating,Rating Date,Rating Comment\n");

        // row
        String ratingVal  = (latest != null) ? String.valueOf(latest.getRatingValue()) : "";
        String ratingDate = (latest != null && latest.getRatingDate() != null)
                ? latest.getRatingDate().toString() : "";
        String ratingComm = (latest != null) ? safe(latest.getComments()) : "";

        sb.append(escapeCsv(String.valueOf(s.getSuppliersId()))).append(",")
          .append(escapeCsv(safe(s.getSuppliersName()))).append(",")
          .append(escapeCsv(safe(s.getSuppliersPhone()))).append(",")
          .append(escapeCsv(safe(s.getSuppliersEmail()))).append(",")
          .append(escapeCsv(safe(s.getSuppliersContactPerson()))).append(",")
          .append(escapeCsv(safe(s.getAddress()))).append(",")
          .append(escapeCsv(ratingVal)).append(",")
          .append(escapeCsv(ratingDate)).append(",")
          .append(escapeCsv(ratingComm)).append("\n");

        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    // ========= ALL SUPPLIERS =========

    public byte[] generateAllSuppliersCsv() {

        List<Supplier> list = supplierRepository.findAll();

        StringBuilder sb = new StringBuilder();

        // header
        sb.append("ID,Name,Phone,Email,Contact Person,Address,Latest Rating,Rating Date,Rating Comment\n");

        for (Supplier s : list) {

            SupplierRating latest = supplierRatingService
                    .getLatestRatingBySupplierId(s.getSuppliersId());

            String ratingVal  = (latest != null) ? String.valueOf(latest.getRatingValue()) : "";
            String ratingDate = (latest != null && latest.getRatingDate() != null)
                    ? latest.getRatingDate().toString() : "";
            String ratingComm = (latest != null) ? safe(latest.getComments()) : "";

            sb.append(escapeCsv(String.valueOf(s.getSuppliersId()))).append(",")
              .append(escapeCsv(safe(s.getSuppliersName()))).append(",")
              .append(escapeCsv(safe(s.getSuppliersPhone()))).append(",")
              .append(escapeCsv(safe(s.getSuppliersEmail()))).append(",")
              .append(escapeCsv(safe(s.getSuppliersContactPerson()))).append(",")
              .append(escapeCsv(safe(s.getAddress()))).append(",")
              .append(escapeCsv(ratingVal)).append(",")
              .append(escapeCsv(ratingDate)).append(",")
              .append(escapeCsv(ratingComm)).append("\n");
        }

        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    // ========= HELPERS =========

    private String safe(String val) {
        return (val == null) ? "" : val;
    }

    private String escapeCsv(String value) {
        if (value == null) return "";
        String v = value.replace("\"", "\"\"");   // escape quotes
        return "\"" + v + "\"";                  // always wrap in quotes
    }
}
