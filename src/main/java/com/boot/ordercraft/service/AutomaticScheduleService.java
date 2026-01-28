package com.boot.ordercraft.service;

import com.boot.ordercraft.model.Product;
import com.boot.ordercraft.model.ProductionSchedule;
import com.boot.ordercraft.repository.ProductionScheduleRepository;
import com.boot.ordercraft.repository.ProductsRepository;
import com.boot.ordercraft.service.MailService.MailService;
import jakarta.transaction.Transactional;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AutomaticScheduleService {
	
	private static final Logger log = LoggerFactory.getLogger(AutomaticScheduleService.class);

    @Autowired
    private ProductsRepository productRepository;

    @Autowired
    private ProductionScheduleRepository scheduleRepository;

    @Autowired
    private MailService emailService;

    // ✅ Auto-schedule every 10 seconds
    @Scheduled(fixedRate = 10000)
    @Transactional
    public void autoScheduleLowStockProducts() {
    	System.out.println("🔄 Checked inventory for low stock products at " + new Date()); 
        List<Product> lowStockProducts = productRepository.findAll().stream()
                .filter(p -> p.getProductsQuantity() <= p.getMinStockThreshold())
                .toList();
        
        if (lowStockProducts.isEmpty()) {
            log.info("⚡ No low stock products found this round.");
        }

        for (Product product : lowStockProducts) {
            // 🔄 Use correct repository method
            boolean alreadyScheduled = scheduleRepository.existsByProductAndPsStatus(product, "PLANNED");
            if (alreadyScheduled) continue;

            // ✅ Calculate required quantity (max cap)
            int requiredQty = product.getMaxStockThreshold() - product.getProductsQuantity();
            if (requiredQty <= 0) continue; // nothing to schedule

            ProductionSchedule schedule = new ProductionSchedule();
            schedule.setProduct(product);
            schedule.setPsStartDate(new Date());
            schedule.setPsEndDate(new Date(System.currentTimeMillis() + 30000)); // 30s later
            schedule.setPsStatus("PLANNED");
            schedule.setPsQuantity(requiredQty); // ✅ important fix

            scheduleRepository.save(schedule);

            System.out.println("✅ Auto-scheduled product: " + product.getProductsName() +
                               " for " + requiredQty + " units");
        }
    }

    // ✅ Check PLANNED schedules and mark COMPLETED after 30s
    @Scheduled(fixedRate = 30000) // check every 30s
    @Transactional
    public void completePlannedProductions() {
        log.info("🔍 [completePlannedProductions] Checking PLANNED schedules at {}", new Date());

        List<ProductionSchedule> plannedSchedules = scheduleRepository.findByPsStatus("PLANNED");

        Date now = new Date();
        for (ProductionSchedule schedule : plannedSchedules) {
            if (schedule.getPsEndDate().before(now)) {
                // ✅ Update schedule status
                schedule.setPsStatus("COMPLETED");
                scheduleRepository.save(schedule);

                Product product = schedule.getProduct();

                // ✅ Increase stock in DB
                int oldQty = product.getProductsQuantity();
                int producedQty = (schedule.getPsQuantity() != null) ? schedule.getPsQuantity() : 0; // ✅ safe
                product.setProductsQuantity(oldQty + producedQty);
                productRepository.save(product);

                // ✅ Send mail
                emailService.sendEmail(
                        "admin@yourcompany.com",
                        "Production Completed",
                        "Production for product " + product.getProductsName() +
                                " is completed. " + producedQty + " units added to stock. " +
                                "New stock = " + product.getProductsQuantity()
                );

                log.info("✅ Completed & updated stock for {} → Old: {}, Produced: {}, New: {}",
                        product.getProductsName(), oldQty, producedQty, product.getProductsQuantity());
            }
        }
    }

}
