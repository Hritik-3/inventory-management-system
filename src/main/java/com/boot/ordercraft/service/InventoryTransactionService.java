package com.boot.ordercraft.service;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boot.ordercraft.model.InventoryTransaction;
import com.boot.ordercraft.model.Product;
import com.boot.ordercraft.repository.InventoryTransactionRepository;
import com.boot.ordercraft.repository.ProductsRepository;

@Service
public class InventoryTransactionService {

    private final InventoryTransactionRepository inventoryTransactionRepository;
    private final ProductsRepository productRepository;

    public InventoryTransactionService(
            InventoryTransactionRepository inventoryTransactionRepository,
            ProductsRepository productRepository) {
        this.inventoryTransactionRepository = inventoryTransactionRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public InventoryTransaction createTransaction(Long productId, String performedBy,
                                                  String type, Integer quantity) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        if (quantity == null || quantity <= 0) {
            throw new RuntimeException("Quantity must be greater than 0");
        }

        if (!type.equalsIgnoreCase("IN") && !type.equalsIgnoreCase("OUT")) {
            throw new RuntimeException("Invalid transaction type. Must be IN or OUT");
        }

        // Adjust product stock
        if (type.equalsIgnoreCase("IN")) {
            product.setProductsQuantity(product.getProductsQuantity() + quantity);
        } else { // OUT
            if (product.getProductsQuantity() < quantity) {
                throw new RuntimeException("Insufficient stock for OUT transaction");
            }
            product.setProductsQuantity(product.getProductsQuantity() - quantity);
        }

        // Save updated product stock
        productRepository.save(product);

        // Create transaction record
        InventoryTransaction tx = new InventoryTransaction();
        tx.setProduct(product);
        tx.setItPerformedBy(performedBy);
        tx.setItTransactionType(type);
        tx.setItQuantity(quantity);
        tx.setItTransactionDate(new Date());

        return inventoryTransactionRepository.save(tx);
    }
}
