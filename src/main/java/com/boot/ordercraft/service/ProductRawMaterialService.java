package com.boot.ordercraft.service;
import com.boot.ordercraft.model.ProductRawMaterial;
import com.boot.ordercraft.model.RawMaterial;
import com.boot.ordercraft.repository.ProductRawMaterialRepository;

import com.boot.ordercraft.repository.RawMaterialsRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductRawMaterialService {

    private final ProductRawMaterialRepository productRawMaterialRepository;
    private final RawMaterialsRepository rawMaterialRepository;

    public ProductRawMaterialService(ProductRawMaterialRepository productRawMaterialRepository,
                                     RawMaterialsRepository rawMaterialRepository) {
        this.productRawMaterialRepository = productRawMaterialRepository;
        this.rawMaterialRepository = rawMaterialRepository;
    }

    /**
     * Get all raw materials required for a product
     */
    public List<ProductRawMaterial> getBomForProduct(Long productId) {
        return productRawMaterialRepository.findByProductId(productId);
    }

    /**
     * Check if raw materials are available for given production quantity
     */
    public boolean hasEnoughRawMaterials(Long productId, int quantityToProduce) {
        List<ProductRawMaterial> bom = getBomForProduct(productId);

        for (ProductRawMaterial prm : bom) {
            RawMaterial rawMaterial = rawMaterialRepository.findById(prm.getRawMaterialId())
                    .orElseThrow(() -> new RuntimeException("Raw material not found: " + prm.getRawMaterialId()));

            double required = prm.getQuantityRequiredPerUnit() * quantityToProduce;
            if (rawMaterial.getRwQuantity() < required) {
                return false; // Not enough material
            }
        }
        return true;
    }

    /**
     * Deduct raw materials when scheduling production
     */
    @Transactional
    public void consumeRawMaterials(Long productId, int quantityToProduce) {
        List<ProductRawMaterial> bom = getBomForProduct(productId);

        for (ProductRawMaterial prm : bom) {
            RawMaterial rawMaterial = rawMaterialRepository.findById(prm.getRawMaterialId())
                    .orElseThrow(() -> new RuntimeException("Raw material not found: " + prm.getRawMaterialId()));

            double required = prm.getQuantityRequiredPerUnit() * quantityToProduce;

            if (rawMaterial.getRwQuantity() < required) {
                throw new RuntimeException("Not enough raw material: " + rawMaterial.getRwName());
            }

            rawMaterial.setRwQuantity((int) (rawMaterial.getRwQuantity() - required));
            rawMaterialRepository.save(rawMaterial);
        }
    }
}
