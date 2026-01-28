package com.boot.ordercraft.model;

import jakarta.persistence.*;

@Entity
@Table(name = "PRODUCT_RAWMATERIAL", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"PRODUCT_ID", "RAW_MATERIAL_ID"})
})
public class ProductRawMaterial {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "PRODUCT_ID", nullable = false)
    private Long productId;

    @Column(name = "RAW_MATERIAL_ID", nullable = false)
    private Long rawMaterialId;

    @Column(name = "QUANTITY_REQUIRED_PER_UNIT", nullable = false)
    private Double quantityRequiredPerUnit;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public Long getRawMaterialId() { return rawMaterialId; }
    public void setRawMaterialId(Long rawMaterialId) { this.rawMaterialId = rawMaterialId; }

    public Double getQuantityRequiredPerUnit() { return quantityRequiredPerUnit; }
    public void setQuantityRequiredPerUnit(Double quantityRequiredPerUnit) { this.quantityRequiredPerUnit = quantityRequiredPerUnit; }
}
