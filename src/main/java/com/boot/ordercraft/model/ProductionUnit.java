package com.boot.ordercraft.model;

import jakarta.persistence.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "PRODUCTION_UNIT")
public class ProductionUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "unit_seq")
    @SequenceGenerator(name = "unit_seq", sequenceName = "UNIT_SEQ", allocationSize = 1)
    @Column(name = "UNIT_ID")
    private Long unitId;

    @Column(name = "UNIT_NAME", nullable = false, length = 100)
    private String unitName;

    // ✅ Remove old String category field
    // @Column(name = "CATEGORY", length = 100)
    // private String category;

    // ✅ Proper relationship with Category
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_ID", nullable = false)
    private Category category;

    @Column(name = "STATUS", length = 50)
    private String status = "AVAILABLE";

    // One unit can have many production lines
    @OneToMany(mappedBy = "productionUnit", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore   // 👈 ADD THIS to stop recursive JSON
    private List<ProductionLine> productionLines;
    // ---------------- Getters and Setters ----------------
    public Long getUnitId() { return unitId; }
    public void setUnitId(Long unitId) { this.unitId = unitId; }

    public String getUnitName() { return unitName; }
    public void setUnitName(String unitName) { this.unitName = unitName; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public String getStatus() {
        if (productionLines != null && productionLines.stream().allMatch(line -> "AVAILABLE".equals(line.getStatus()))) {
            status = "AVAILABLE";
        } else if (productionLines != null && productionLines.stream().anyMatch(line -> "RUNNING".equals(line.getStatus()))) {
            status = "PARTIALLY OCCUPIED";
        } else {
            status = "UNAVAILABLE";
        }
        return status;
    }

    public void setStatus(String status) { this.status = status; }

    public List<ProductionLine> getProductionLines() { return productionLines; }
    public void setProductionLines(List<ProductionLine> productionLines) { this.productionLines = productionLines; }
}
