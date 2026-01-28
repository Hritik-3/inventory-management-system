//////put this for order status scheduler
////package com.boot.ordercraft.service;
//// 
////import com.boot.ordercraft.model.PurchaseOrder;
////import com.boot.ordercraft.repository.PurchaseOrdersRepository;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.scheduling.annotation.Scheduled;
////import org.springframework.stereotype.Component;
////import org.springframework.context.event.ContextRefreshedEvent;
////import org.springframework.context.event.EventListener;
//// 
////import java.time.LocalDate;
////import java.time.temporal.ChronoUnit;
////import java.util.List;
//// 
////@Component
////public class OrderStatusScheduler {
//// 
////    @Autowired
////    private PurchaseOrdersRepository purchaseOrdersRepository;
//// 
////    // Runs once immediately after Spring context is refreshed (i.e., app startup)
////    @EventListener(ContextRefreshedEvent.class)
////    public void onApplicationEvent() {
////        updateOrderStatus();
////    }
//// 
////    // Runs every 1 minute as per your existing cron expression
////    @Scheduled(cron = "0 */1 * * * *")
////    public void updateOrderStatus() {
////        List<PurchaseOrder> orders = purchaseOrdersRepository.findAll();
//// 
////        LocalDate today = LocalDate.now();
//// 
////        for (PurchaseOrder order : orders) {
////            if (order.getPoOrderDate() == null) {
////                System.out.println("PO ID " + order.getPoId() + ": Skipped - Order Date is null");
////                continue;
////            }
//// 
////            if (order.getPoExpectedDelivery_date() == null) {
////                LocalDate expected = order.getPoOrderDate().toLocalDate().plusDays(7);
////                order.setPoExpectedDelivery_date(expected);
////                System.out.println("PO ID " + order.getPoId() + ": Expected Delivery Date auto-set to " + expected);
////            }
//// 
////            LocalDate orderDate = order.getPoOrderDate().toLocalDate();
////            LocalDate expectedDate = order.getPoExpectedDelivery_date();
//// 
////            long totalDuration = ChronoUnit.DAYS.between(orderDate, expectedDate);
////            long progressDays = ChronoUnit.DAYS.between(orderDate, today);
//// 
////            if (totalDuration <= 0) {
////                order.setPoDeliveryStatus("DELIVERED");
////            } else {
////                double percentComplete = (progressDays * 100.0) / totalDuration;
//// 
////                if (percentComplete <= 25.0) {
////                    order.setPoDeliveryStatus("PENDING");
////                } else if (percentComplete <= 60.0) {
////                    order.setPoDeliveryStatus("IN-PROGRESS");
////                } else if (percentComplete <= 90.0) {
////                    order.setPoDeliveryStatus("DISPATCHED");
////                } else {
////                    order.setPoDeliveryStatus("DELIVERED");
////                }
//// 
////                System.out.printf("PO ID %d: %.1f%% complete, status set to %s%n",
////                        order.getPoId(), percentComplete, order.getPoDeliveryStatus());
////            }
//// 
////            purchaseOrdersRepository.save(order);
////        }
////    }
////}
//// 
//
//
//package com.boot.ordercraft.service;
//
//import com.boot.ordercraft.model.PurchaseOrder;
//import com.boot.ordercraft.repository.PurchaseOrdersRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import org.springframework.context.event.ContextRefreshedEvent;
//import org.springframework.context.event.EventListener;
//
//import java.time.LocalDateTime;
//import java.time.temporal.ChronoUnit;
//import java.util.List;
//
//@Component
//public class OrderStatusScheduler {
//
//    @Autowired
//    private PurchaseOrdersRepository purchaseOrdersRepository;
//
//    // Runs once immediately after Spring context is refreshed (i.e., app startup)
//    @EventListener(ContextRefreshedEvent.class)
//    public void onApplicationEvent() {
//        updateOrderStatus();
//    }
//
//    // Runs every 30 seconds (faster than 1 minute to ensure quick delivery)
//    @Scheduled(fixedRate = 30000) // 30 seconds
//    public void updateOrderStatus() {
//        List<PurchaseOrder> orders = purchaseOrdersRepository.findAll();
//        LocalDateTime now = LocalDateTime.now();
//
//        for (PurchaseOrder order : orders) {
//            if (order.getPoOrderDate() == null) continue;
//
//            // Only update orders that are not delivered yet
//            if (!"DELIVERED".equalsIgnoreCase(order.getPoDeliveryStatus())) {
//                long minutesSinceCreation = ChronoUnit.MINUTES.between(order.getPoOrderDate(), now);
//                
//                // If more than 1 minute has passed since creation, mark as DELIVERED
//                if (minutesSinceCreation >= 1) {
//                    order.setPoDeliveryStatus("DELIVERED");
//                    System.out.println("PO ID " + order.getPoId() + " marked as DELIVERED automatically.");
//                    purchaseOrdersRepository.save(order);
//                }
//            }
//        }
//    }
//}
