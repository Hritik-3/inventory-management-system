package com.boot.ordercraft.dto;

import java.util.List;

import com.boot.ordercraft.model.Product;
import com.boot.ordercraft.model.ProductRawMaterial;
import com.boot.ordercraft.model.ProductionLine;
import com.boot.ordercraft.model.ProductionSchedule;
import com.boot.ordercraft.model.RawMaterial;

public class ProductionManagerDashboardDTO {

    private List<Product> products;
    private List<RawMaterial> rawMaterials;
    private List<ProductionLine> productionLines;
    private List<ProductionSchedule> productionSchedules;
    private List<ProductRawMaterial> productRawMaterials;

    public List<Product> getProducts() {
        return products;
    }
    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<RawMaterial> getRawMaterials() {
        return rawMaterials;
    }
    public void setRawMaterials(List<RawMaterial> rawMaterials) {
        this.rawMaterials = rawMaterials;
    }

    public List<ProductionLine> getProductionLines() {
        return productionLines;
    }
    public void setProductionLines(List<ProductionLine> productionLines) {
        this.productionLines = productionLines;
    }

    public List<ProductionSchedule> getProductionSchedules() {
        return productionSchedules;
    }
    public void setProductionSchedules(List<ProductionSchedule> productionSchedules) {
        this.productionSchedules = productionSchedules;
    }

    public List<ProductRawMaterial> getProductRawMaterials() {
        return productRawMaterials;
    }
    public void setProductRawMaterials(List<ProductRawMaterial> productRawMaterials) {
        this.productRawMaterials = productRawMaterials;
    }
}
