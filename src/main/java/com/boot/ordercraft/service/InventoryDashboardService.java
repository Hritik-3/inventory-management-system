package com.boot.ordercraft.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boot.ordercraft.dto.DashboardDTO;
import com.boot.ordercraft.dto.ProductDistributionDTO;
import com.boot.ordercraft.model.Product;
import com.boot.ordercraft.repository.ProductsRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryDashboardService {
    
    @Autowired
    private ProductsRepository productRepository;

    public DashboardDTO getDashboardData() {
        DashboardDTO dto = new DashboardDTO();

        List<Product> products = productRepository.findAll();

        int totalProducts = products.size();

        BigDecimal stockValue = products.stream()
                .filter(p -> p.getProductsQuantity() != null && p.getProductsUnitPrice() != null)
                .map(p -> BigDecimal.valueOf(p.getProductsQuantity())
                        .multiply(BigDecimal.valueOf(p.getProductsUnitPrice())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int lowStockAlerts = (int) products.stream()
                .filter(p -> p.getProductsQuantity() != null && p.getMinStockThreshold() != null)
                .filter(p -> p.getProductsQuantity() > 0 && p.getProductsQuantity() < p.getMinStockThreshold())
                .count();

        int outOfStock = (int) products.stream()
                .filter(p -> p.getProductsQuantity() != null && p.getProductsQuantity() == 0)
                .count();

        int overstockItems = (int) products.stream()
                .filter(p -> p.getProductsQuantity() != null && p.getMaxStockThreshold() != null)
                .filter(p -> p.getProductsQuantity() > p.getMaxStockThreshold())
                .count();

        // ✅ Use DTO instead of Map.of
        List<ProductDistributionDTO> productDistribution = products.stream()
                .map(p -> new ProductDistributionDTO(
                        p.getProductsName(),
                        p.getProductsQuantity() != null ? p.getProductsQuantity() : 0
                ))
                .collect(Collectors.toList());

        dto.setTotalProducts(totalProducts);
        dto.setStockValue(stockValue);
        dto.setLowStockAlerts(lowStockAlerts);
        dto.setOutOfStock(outOfStock);
        dto.setOverstockItems(overstockItems);
        dto.setProductDistribution(productDistribution);

        return dto;
    }
}
