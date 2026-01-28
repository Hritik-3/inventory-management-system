package com.boot.ordercraft.service;

import com.boot.ordercraft.dto.ProductDTO;
import com.boot.ordercraft.dto.ProductionScheduleCreateRequest;
import com.boot.ordercraft.dto.ProductionScheduleResponse;
import com.boot.ordercraft.dto.RawMaterialCheckResponse;
import com.boot.ordercraft.model.*;
import com.boot.ordercraft.repository.*;
import com.boot.ordercraft.service.MailService.MailService;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * ProductionScheduleService
 * - raw material check & reservation
 * - allocate across lines (respecting capacity + category)
 * - hold leftover in memory queue
 * - incremental production (20 units / 10s per schedule chunk)
 * - automatic re-assignment of held quantity when a line becomes free
 * - send email on completion
 */
@Service
public class ProductionScheduleService {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(8);
    private final ConcurrentLinkedQueue<HeldOrder> heldQueue = new ConcurrentLinkedQueue<>();

    private final ProductionScheduleRepository scheduleRepo;
    private final ProductsRepository productRepo;
    private final InventoryTransactionRepository transactionRepo;
    private final ProductionScheduleTrackingService trackingService;
    private final ProductionLineRepository lineRepo;
    private final ProductRawMaterialRepository productRawMaterialRepo;
    private final RawMaterialsRepository rawMaterialRepo;
    private final MailService mailService;

    public ProductionScheduleService(ProductionScheduleRepository scheduleRepo,
                                     ProductsRepository productRepo,
                                     InventoryTransactionRepository transactionRepo,
                                     ProductionScheduleTrackingService trackingService,
                                     ProductionLineRepository lineRepo,
                                     ProductRawMaterialRepository productRawMaterialRepo,
                                     RawMaterialsRepository rawMaterialRepo,
                                     MailService mailService) {
        this.scheduleRepo = scheduleRepo;
        this.productRepo = productRepo;
        this.transactionRepo = transactionRepo;
        this.trackingService = trackingService;
        this.lineRepo = lineRepo;
        this.productRawMaterialRepo = productRawMaterialRepo;
        this.rawMaterialRepo = rawMaterialRepo;
        this.mailService = mailService;
    }

    // -------------------------------
    // CREATE PRODUCTION ORDER
    // -------------------------------
    @Transactional
    public Object createProductionOrder(ProductionScheduleCreateRequest request) {
        Product product = productRepo.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        int requestedQty = (request.getQuantity() != null && request.getQuantity() > 0)
                ? request.getQuantity()
                : 1;

        // 1️⃣ Check BOM raw materials
        List<ProductRawMaterial> bom = productRawMaterialRepo.findByProductId(product.getProductsId());
        List<String> sufficientList = new ArrayList<>();
        List<String> insufficientList = new ArrayList<>();

        for (ProductRawMaterial prm : bom) {
            RawMaterial raw = rawMaterialRepo.findById(prm.getRawMaterialId())
                    .orElseThrow(() -> new RuntimeException("Raw material not found: " + prm.getRawMaterialId()));
            double need = prm.getQuantityRequiredPerUnit() * requestedQty;
            if (raw.getRwQuantity() >= need) {
                sufficientList.add(raw.getRwName() + " ✅ (Have: " + raw.getRwQuantity() + ", Need: " + need + ")");
            } else {
                insufficientList.add(raw.getRwName() + " ❌ (Have: " + raw.getRwQuantity() + ", Need: " + need + ")");
            }
        }

        if (!insufficientList.isEmpty()) {
            return new RawMaterialCheckResponse(false, sufficientList, insufficientList);
        }

        // 2️⃣ Reserve raw materials
        for (ProductRawMaterial prm : bom) {
            RawMaterial raw = rawMaterialRepo.findById(prm.getRawMaterialId()).get();
            double need = prm.getQuantityRequiredPerUnit() * requestedQty;
            raw.setRwQuantity((int) (raw.getRwQuantity() - need));
            rawMaterialRepo.save(raw);
        }

        // 3️⃣ Allocate on available lines
        Long categoryId = product.getCategory().getCategoriesId();
        int remaining = requestedQty;
        List<ProductionScheduleResponse> createdResponses = new ArrayList<>();

        remaining = allocateToAvailableLines(product, categoryId, remaining, request.getStartDate(), request.getEndDate(), createdResponses);

        // 4️⃣ Hold leftover if any
        HeldAcknowledgement heldAck = null;
        if (remaining > 0) {
            HeldOrder held = new HeldOrder(product.getProductsId(), remaining, categoryId, request.getStartDate(), request.getEndDate());
            heldQueue.add(held);
            heldAck = new HeldAcknowledgement(true, product.getProductsId(), remaining);
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("allocatedSchedules", createdResponses);
        if (heldAck != null) result.put("held", heldAck);
        result.put("message", "Raw materials reserved. Allocated on " + createdResponses.size() + " line(s)." +
                (heldAck != null ? " Remaining " + remaining + " held until lines free." : ""));

        return result;
    }

    // -------------------------------
    // ALLOCATE TO AVAILABLE LINES
    // -------------------------------
    private int allocateToAvailableLines(Product product, Long categoryId, int quantity,
                                         Date startDate, Date endDate,
                                         List<ProductionScheduleResponse> responseList) {

        List<ProductionLine> availableLines = lineRepo.findByStatusAndProductionUnit_Category_CategoriesId("AVAILABLE", categoryId);
        availableLines.sort(Comparator.comparingInt(l -> -Optional.ofNullable(l.getCapacity()).orElse(0)));

        int remaining = quantity;

        for (ProductionLine line : availableLines) {
            if (remaining <= 0) break;

            int capacity = Optional.ofNullable(line.getCapacity()).orElse(0);
            if (capacity <= 0) continue;

            int assignQty = Math.min(capacity, remaining);

            // Mark line as BUSY
            line.setStatus("BUSY");
            lineRepo.save(line);

            // Create production schedule
            ProductionSchedule chunk = new ProductionSchedule();
            chunk.setProduct(product);
            chunk.setProductionLine(line);
            chunk.setPsStartDate(startDate);
            chunk.setPsEndDate(endDate);
            chunk.setPsQuantity(assignQty);
            chunk.setPsStatus("UNDER_PRODUCTION");
            chunk = scheduleRepo.save(chunk);

            ProductionScheduleResponse resp = new ProductionScheduleResponse(
                    chunk.getPsId(),
                    product.getProductsName(),
                    chunk.getPsQuantity(),
                    chunk.getPsStatus(),
                    line.getProductionUnit().getUnitName() + " - " + line.getLineName()
            );
            resp.setProductId(product.getProductsId());
            resp.setStartDate(startDate);
            resp.setEndDate(endDate);
            resp.setMessage("Allocated and production started on line: " + line.getLineName());
            resp.setCurrentStatus("UNDER_PRODUCTION");
            responseList.add(resp);

            startIncrementalProduction(chunk, resp);

            remaining -= assignQty;
        }

        return remaining;
    }

    // -------------------------------
    // INCREMENTAL PRODUCTION
    // -------------------------------
    private void startIncrementalProduction(ProductionSchedule schedule, ProductionScheduleResponse response) {
        final int batch = 20;
        final int intervalSeconds = 10;
        final int total = schedule.getPsQuantity();
        final int[] produced = {0};

        // Use wrapper array to hold future
        final ScheduledFuture<?>[] futureHolder = new ScheduledFuture<?>[1];

        futureHolder[0] = scheduler.scheduleAtFixedRate(() -> {
            try {
                int remainingChunk = total - produced[0];
                if (remainingChunk <= 0) {
                    if (futureHolder[0] != null) futureHolder[0].cancel(false);
                    return;
                }

                int produceNow = Math.min(batch, remainingChunk);
                produced[0] += produceNow;

                Product product = schedule.getProduct();
                product.setProductsQuantity(product.getProductsQuantity() + produceNow);
                productRepo.save(product);

                if (produced[0] >= total) {
                    schedule.setPsStatus("COMPLETED");
                    scheduleRepo.save(schedule);

                    InventoryTransaction txn = new InventoryTransaction();
                    txn.setProduct(product);
                    txn.setItPerformedBy("AUTO_SYSTEM");
                    txn.setItTransactionDate(new Date());
                    txn.setItTransactionType("IN");
                    txn.setItQuantity(total);
                    transactionRepo.save(txn);

                    trackingService.trackOrderStatus(schedule.getPsId());

                    ProductionLine line = schedule.getProductionLine();
                    if (line != null) {
                        line.setStatus("AVAILABLE");
                        lineRepo.save(line);
                    }

                    try {
                        String subject = "Production schedule completed: ID " + schedule.getPsId();
                        String body = "Schedule " + schedule.getPsId() + " for product " +
                                product.getProductsName() + " is COMPLETED. Quantity: " + total +
                                ". Line released: " + (line != null ? line.getLineName() : "n/a");
                        mailService.sendSimpleMessage("admin@ordercraft.com", subject, body);
                    } catch (Exception ex) {
                        System.err.println("Failed to send completion email: " + ex.getMessage());
                    }

                    response.setCurrentStatus("COMPLETED");

                    // Cancel scheduled task
                    if (futureHolder[0] != null) futureHolder[0].cancel(false);

                    // Re-allocate held orders
                    assignHeldOrdersToAvailableLines();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, intervalSeconds, TimeUnit.SECONDS);
    }


    // -------------------------------
    // ASSIGN HELD ORDERS TO FREE LINES
    // -------------------------------
    private void assignHeldOrdersToAvailableLines() {
        List<ProductionLine> freeLines = lineRepo.findByStatus("AVAILABLE");
        if (freeLines.isEmpty()) return;

        Iterator<HeldOrder> iter;
        while (!freeLines.isEmpty() && (iter = heldQueue.iterator()).hasNext()) {
            HeldOrder held = heldQueue.poll();
            if (held == null) break;

            int remaining = held.remainingQty;
            Product product = productRepo.findById(held.productId).orElse(null);
            if (product == null) continue;

            List<ProductionLine> linesToUse = freeLines.stream()
                    .filter(l -> l.getProductionUnit().getCategory().getCategoriesId().equals(held.categoryId))
                    .sorted(Comparator.comparingInt(l -> -Optional.ofNullable(l.getCapacity()).orElse(0)))
                    .collect(Collectors.toList());

            for (ProductionLine line : linesToUse) {
                if (remaining <= 0) break;
                int cap = Optional.ofNullable(line.getCapacity()).orElse(0);
                if (cap <= 0) continue;

                int assign = Math.min(cap, remaining);

                line.setStatus("BUSY");
                lineRepo.save(line);

                ProductionSchedule chunk = new ProductionSchedule();
                chunk.setProduct(product);
                chunk.setProductionLine(line);
                chunk.setPsStartDate(held.startDate);
                chunk.setPsEndDate(held.endDate);
                chunk.setPsQuantity(assign);
                chunk.setPsStatus("UNDER_PRODUCTION");
                chunk = scheduleRepo.save(chunk);

                ProductionScheduleResponse resp = new ProductionScheduleResponse(
                        chunk.getPsId(),
                        product.getProductsName(),
                        chunk.getPsQuantity(),
                        chunk.getPsStatus(),
                        line.getProductionUnit().getUnitName() + " - " + line.getLineName()
                );
                resp.setProductId(product.getProductsId());
                resp.setStartDate(chunk.getPsStartDate());
                resp.setEndDate(chunk.getPsEndDate());
                resp.setMessage("Held quantity now allocated to line " + line.getLineName());
                resp.setCurrentStatus("UNDER_PRODUCTION");

                startIncrementalProduction(chunk, resp);

                remaining -= assign;
                freeLines.remove(line);
            }

            if (remaining > 0) {
                held.remainingQty = remaining;
                heldQueue.add(held);
                break;
            }
        }
    }

    // -------------------------------
    // HELD ORDER & ACK CLASSES
    // -------------------------------
    private static class HeldOrder {
        final Long productId;
        final Long categoryId;
        int remainingQty;
        final Date startDate;
        final Date endDate;

        HeldOrder(Long productId, int remainingQty, Long categoryId, Date startDate, Date endDate) {
            this.productId = productId;
            this.remainingQty = remainingQty;
            this.categoryId = categoryId;
            this.startDate = startDate;
            this.endDate = endDate;
        }
    }

    private static class HeldAcknowledgement {
        public final boolean held;
        public final Long productId;
        public final int qtyHeld;

        HeldAcknowledgement(boolean held, Long productId, int qtyHeld) {
            this.held = held;
            this.productId = productId;
            this.qtyHeld = qtyHeld;
        }
    }

    // -------------------------------
    // MANUAL COMPLETION
    // -------------------------------
    @Transactional
    public void completeProductionOrder(Long scheduleId, String performedBy) {
        ProductionSchedule schedule = scheduleRepo.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        if (!"PLANNED".equals(schedule.getPsStatus()) && !"UNDER_PRODUCTION".equals(schedule.getPsStatus())) {
            throw new RuntimeException("Only PLANNED or UNDER_PRODUCTION schedules can be completed!");
        }

        ProductionLine line = schedule.getProductionLine();
        if (line != null) {
            line.setStatus("AVAILABLE");
            lineRepo.save(line);
        }

        updateScheduleToCompleted(schedule, performedBy);
        assignHeldOrdersToAvailableLines();
    }

    @Transactional
    public void completeAllPlannedOrders(String performedBy) {
        List<ProductionSchedule> plannedSchedules = scheduleRepo.findByPsStatus("PLANNED");
        if (plannedSchedules.isEmpty()) return;

        for (ProductionSchedule schedule : plannedSchedules) {
            ProductionLine line = schedule.getProductionLine();
            if (line != null) {
                line.setStatus("AVAILABLE");
                lineRepo.save(line);
            }
            updateScheduleToCompleted(schedule, performedBy);
        }

        assignHeldOrdersToAvailableLines();
    }

    @Transactional
    private void updateScheduleToCompleted(ProductionSchedule schedule, String performedBy) {
        schedule.setPsStatus("COMPLETED");
        scheduleRepo.save(schedule);

        Product product = schedule.getProduct();
        product.setProductsQuantity(product.getProductsQuantity() + schedule.getPsQuantity());
        productRepo.save(product);

        InventoryTransaction txn = new InventoryTransaction();
        txn.setProduct(product);
        txn.setItPerformedBy(performedBy);
        txn.setItTransactionDate(new Date());
        txn.setItTransactionType("IN");
        txn.setItQuantity(schedule.getPsQuantity());
        transactionRepo.save(txn);

        try {
            String subject = "Production completed: schedule " + schedule.getPsId();
            String body = "Schedule " + schedule.getPsId() + " for product " + product.getProductsName()
                    + " completed. Quantity: " + schedule.getPsQuantity();
            mailService.sendSimpleMessage("admin@ordercraft.com", subject, body);
        } catch (Exception e) {
            System.err.println("Mail send failed: " + e.getMessage());
        }

        trackingService.trackOrderStatus(schedule.getPsId());
    }

    // -------------------------------
    // LOW STOCK LOGIC
    // -------------------------------
    public List<Product> getLowStockProducts() {
        return productRepo.findLowStockProducts();
    }

    public void handleLowStockAutoSchedule(String performedBy) {
        List<Product> lowStockProducts = productRepo.findLowStockProducts();

        for (Product product : lowStockProducts) {
            int currentQty = product.getProductsQuantity();
            int minThreshold = product.getMinStockThreshold();
            int maxThreshold = product.getMaxStockThreshold();

            if (currentQty <= minThreshold) {
                int requiredQty = maxThreshold - currentQty;
                ProductionLine line = lineRepo.findFirstByStatus("AVAILABLE").orElse(null);
                if (line != null) {
                    line.setStatus("BUSY");
                    lineRepo.save(line);

                    ProductionSchedule schedule = new ProductionSchedule();
                    schedule.setProduct(product);
                    schedule.setProductionLine(line);
                    schedule.setPsStartDate(new Date());
                    schedule.setPsEndDate(new Date());
                    schedule.setPsQuantity(requiredQty);
                    schedule.setPsStatus("PLANNED");
                    scheduleRepo.save(schedule);

                    System.out.println("⚠️ Low stock for: " + product.getProductsName() +
                            ". Auto-scheduled " + requiredQty + " units.");
                } else {
                    System.out.println("⚠️ Low stock but no line available for: " + product.getProductsName());
                }
            }
        }
    }

    public List<ProductDTO> getLowStockProductsWithScheduleStatus() {
        return productRepo.findLowStockProducts().stream().map(p -> {
            boolean scheduled = scheduleRepo.existsByProductAndPsStatusIgnoreCase(p, "PLANNED");
            return new ProductDTO(
                    p.getProductsId(),
                    p.getProductsName(),
                    p.getProductsUnitPrice(),
                    p.getProductsQuantity(),
                    scheduled
            );
        }).toList();
    }
    
    public List<ProductionSchedule> getAllSchedules() {
        return scheduleRepo.findAll(); // fetches all schedules from DB
    }
}
