package com.boot.ordercraft.service;


import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.boot.ordercraft.model.LowStockAlert;
import com.boot.ordercraft.model.Product;
import com.boot.ordercraft.repository.LowStockAlertRepository;
import com.boot.ordercraft.repository.ProductsRepository;

import java.time.Instant;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class LowStockAlertService {
	
	
	private static final Logger logger = LoggerFactory.getLogger(LowStockAlertService.class);
	 
    private final LowStockAlertRepository alertRepo;
    private final ProductsRepository productRepo;
 
    public LowStockAlertService(LowStockAlertRepository alertRepo, ProductsRepository productRepo) {
        this.alertRepo = alertRepo;
        this.productRepo = productRepo;
    }
 
    @Scheduled(fixedRate = 5000) // every 5 sec 
    public void autoCheckStockLevels() {
        logger.info("Running scheduled stock check...");
        checkAndGenerateAlerts();
    }
 
    public void checkAndGenerateAlerts() {
        List<Product> products = productRepo.findAll();
        for (Product product : products) {
            if (product.getProductsQuantity() < product.getMinStockThreshold()) {
                boolean alreadyExists = alertRepo.existsByProductIdAndStatus(product.getProductsId(), "ACKNOWLEDGED");
                if (!alreadyExists) {
                    LowStockAlert alert = new LowStockAlert();
                    alert.setProductId(product.getProductsId());
                    alert.setProductName(product.getProductsName());
                    alert.setCurrentStock(product.getProductsQuantity());
                    alert.setThreshold(product.getMinStockThreshold());
                    alert.setStatus("NEW");
                    alert.setCreatedAt(Instant.now());
                    alertRepo.save(alert);
                    logger.warn("Low stock alert created for product: {}", product.getProductsName());
                }
            }
        }
    }
 
    public List<LowStockAlert> getAllAlerts() {
        return alertRepo.findAll();
    }
 
    public LowStockAlert acknowledgeAlert(Long id) {
        LowStockAlert alert = alertRepo.findById(id).orElseThrow();
        alert.setStatus("ACKNOWLEDGED");
        return alertRepo.save(alert);
    }

}
