package com.boot.ordercraft.service;
 
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
 
import com.boot.ordercraft.dto.InvoiceResponseDTO;
import com.boot.ordercraft.dto.OrderforCustomer;
import com.boot.ordercraft.dto.PurchaseOrderCreateRequest;
import com.boot.ordercraft.dto.PurchaseOrderInternalRequest;
import com.boot.ordercraft.dto.PurchaseOrderItemRequest;
import com.boot.ordercraft.dto.PurchaseOrderUpdateRequest;
import com.boot.ordercraft.model.Customer;
import com.boot.ordercraft.model.Product;
import com.boot.ordercraft.model.PurchaseOrder;
import com.boot.ordercraft.model.PurchaseOrderItem;
import com.boot.ordercraft.model.RawMaterial;
import com.boot.ordercraft.model.Supplier;
import com.boot.ordercraft.model.User;
import com.boot.ordercraft.repository.CustomerRepository;
import com.boot.ordercraft.repository.ProductsRepository;
import com.boot.ordercraft.repository.PurchaseOrderItemsRepository;
import com.boot.ordercraft.repository.PurchaseOrdersRepository;
import com.boot.ordercraft.repository.RawMaterialsRepository;
import com.boot.ordercraft.repository.SuppliersRepository;
import com.boot.ordercraft.repository.UserRepository;
import com.boot.ordercraft.service.MailService.MailService;
import com.boot.ordercraft.util.InvoicePdfGenerator;
 
import jakarta.transaction.Transactional;
 
@Service
public class PurchaseOrderService {
 
    @Autowired
    private PurchaseOrdersRepository purchaseOrderRepository;
 
    @Autowired
    private PurchaseOrderItemsRepository purchaseOrderItemRepository;
 
    @Autowired
    private SuppliersRepository supplierRepository;
 
    @Autowired
    private ProductsRepository productRepository;
 
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RawMaterialsRepository rawMaterialsRepository;
    
    @Autowired
    private CustomerRepository customerRepo;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private InvoiceService invoiceService;
 
    @Autowired
    private MailService emailService;
 
//Order to supplier
//-----------------------------------------------------------------------------------------------------------------
    public ResponseEntity<String> createPurchaseOrder(PurchaseOrderCreateRequest request, String username) {

        System.out.println("Authenticated principal name: " + username);

        // Fetch user
        Optional<User> userOpt = userService.getUserByUsername(username);
        if (userOpt.isEmpty()) userOpt = userService.getByEmail(username);
        if (userOpt.isEmpty()) {
            System.out.println("User not found for username/email: " + username);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
        User user = userOpt.get();
        System.out.println("User ID: " + user.getUserId());

        // Validate supplier
        System.out.println("Supplier ID from request: " + request.getSupplierId());
        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> {
                    System.out.println("Supplier not found with ID: " + request.getSupplierId());
                    return new RuntimeException("Supplier not found");
                });
        System.out.println("Supplier found: " + supplier.getSuppliersName());

        // Create PurchaseOrder entity
        PurchaseOrder order = new PurchaseOrder();
        order.setPoOrderDate(request.getPoOrderDate() != null ? request.getPoOrderDate() : LocalDateTime.now());

        // Convert Date to LocalDate
        LocalDate expectedDate;
        if (request.getPoExpectedDelivery_Date() != null) {
            expectedDate = request.getPoExpectedDelivery_Date().toInstant()
                             .atZone(ZoneId.systemDefault())
                             .toLocalDate();
        } else {
            expectedDate = LocalDate.now().plusDays(7);
        }
        order.setPoExpectedDelivery_date(expectedDate);

        order.setUser(user);
        order.setSupplier(supplier);
        order.setPoDeliveryStatus("INITIATED");

        // ---------------------
        // Correct PO type assignment (supplier order)
        // ---------------------
        // This explicitly marks this saved order as a SUPPLIER order.
        order.setPoOrderType("SUPPLIER_ORDER");

        // Save order
        PurchaseOrder savedOrder = purchaseOrderRepository.save(order);
        System.out.println("Saved Order ID: " + savedOrder.getPoId());

        // Save items
        for (PurchaseOrderItemRequest itemReq : request.getItems()) {
            System.out.println("Processing item with RW ID: " + itemReq.getrWId() + ", Quantity: " + itemReq.getQuantity());

            RawMaterial rawMaterial = rawMaterialsRepository.findById(itemReq.getrWId())
                    .orElseThrow(() -> {
                        System.out.println("Raw Material not found with ID: " + itemReq.getrWId());
                        return new RuntimeException("Raw Material not found with ID: " + itemReq.getrWId());
                    });
            System.out.println("Raw Material found: " + rawMaterial.getRwName() + ", Available stock: " + rawMaterial.getRwQuantity());

            if (rawMaterial.getRwQuantity() < itemReq.getQuantity()) {
                System.out.println("Insufficient stock for product: " + rawMaterial.getRwName());
                throw new IllegalArgumentException("Insufficient stock for product: " + rawMaterial.getRwName());
            }

            PurchaseOrderItem item = new PurchaseOrderItem();
            item.setPurchaseOrder(savedOrder);
            item.setRawmaterial(rawMaterial);
            item.setPoiQuantity(itemReq.getQuantity());
            item.setPoiCost(itemReq.getCost());

            // Reduce stock
            rawMaterial.setRwQuantity(rawMaterial.getRwQuantity() - itemReq.getQuantity());
            rawMaterialsRepository.save(rawMaterial);

            purchaseOrderItemRepository.save(item);

            System.out.println("Saved item: " + rawMaterial.getRwName() + ", Qty: " + itemReq.getQuantity());
        }

        System.out.println("Order placed successfully. Order ID: " + savedOrder.getPoId());
        return ResponseEntity.ok("Order placed successfully. Order ID: " + savedOrder.getPoId());
    }

//-----------------------------------------------------------------------------------------------------------------
   
 // 🔹 ORDER FOR CUSTOMER (INTERNAL ORDER FLOW)
    public void createInternalOrder(PurchaseOrderInternalRequest request, String username) {
        // 🔹 Who created the order (Procurement Officer)
        User createdByUser = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Creator user not found"));

        // 🔹 Internal Supplier (Inventory Manager - always BGSW)
        Supplier inventoryManagerSupplier = supplierRepository.findBySuppliersName("BGSW")
                .orElseThrow(() -> new RuntimeException("Inventory Manager supplier not found"));

        // 🔹 Customer placing the order
        Customer customer = customerRepo.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // 🔹 Create the order
        PurchaseOrder order = new PurchaseOrder();
        order.setPoOrderDate(LocalDateTime.now());
        order.setPoExpectedDelivery_date(
                request.getExpectedDate() != null ? request.getExpectedDate() : LocalDate.now().plusDays(3)
        );

        // ----------------------------------------------------
        // Initial Order Setup
        // ----------------------------------------------------
        order.setPoOrderType("CUSTOMER_ORDER");
        order.setPoDeliveryStatus("CREATED"); // ✅ Status stays CREATED at creation
        order.setSupplier(inventoryManagerSupplier);
        order.setUser(createdByUser);
        order.setCustomer(customer);

        // Save initial order
        PurchaseOrder savedOrder = purchaseOrderRepository.save(order);

        // ----------------------------------------------------
        // Add Items (no stock validation yet)
        // ----------------------------------------------------
        List<PurchaseOrderItem> itemList = new ArrayList<>();

        for (OrderforCustomer itemReq : request.getItems()) {
            Product product = productRepository.findById(itemReq.getProductsId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            PurchaseOrderItem item = new PurchaseOrderItem();
            item.setPurchaseOrder(savedOrder);
            item.setProduct(product);
            item.setPoiQuantity(itemReq.getQuantity());
            item.setPoiCost(product.getProductsUnitPrice());
            purchaseOrderItemRepository.save(item);
            itemList.add(item);
        }

        savedOrder.setItems(itemList);
        purchaseOrderRepository.save(savedOrder);

        // ----------------------------------------------------
        // Generate and Email Invoice
        // ----------------------------------------------------
        try {
            InvoiceResponseDTO invoice = invoiceService.generateInvoice(savedOrder.getPoId());
            byte[] pdf = InvoicePdfGenerator.generatePdf(invoice);
            String recipientEmail = savedOrder.getUser().getUserEmail();
            emailService.sendInvoiceEmail(recipientEmail, pdf, savedOrder.getPoId().toString());
            System.out.println("✅ Invoice sent to customer: " + recipientEmail);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    // ----------------------------------------------------------------------------------------------------
//    // 🔹 UPDATE ORDER STATUS / ITEMS — restricted to CUSTOMER_ORDER type
//    // ----------------------------------------------------------------------------------------------------
//    @Transactional
//    public void updateOrderAndItems(Long orderId, PurchaseOrderUpdateRequest request, String username) {
//        PurchaseOrder order = purchaseOrderRepository.findById(orderId)
//                .orElseThrow(() -> new RuntimeException("Order not found"));
//
//        // ----------------------------------------------------
//        // 1️⃣ Type Protection — Only Customer/Internal Orders
//        // ----------------------------------------------------
//        if (!"CUSTOMER_ORDER".equalsIgnoreCase(order.getPoOrderType())) {
//            throw new RuntimeException("This operation is only allowed for CUSTOMER_ORDER type.");
//        }
//
//        // ----------------------------------------------------
//        // 2️⃣ Validate Order Status Flow
//        // ----------------------------------------------------
//        String currentStatus = order.getPoDeliveryStatus();
//        String newStatus = request.getNewDeliveryStatus();
//
//        if (newStatus != null) {
//            switch (currentStatus) {
//                case "CREATED":
//                    if (!newStatus.equals("ACCEPTED") && !newStatus.equals("SENT_FOR_PRODUCTION")) {
//                        throw new RuntimeException("Invalid transition from CREATED → " + newStatus);
//                    }
//                    break;
//
//                case "SENT_FOR_PRODUCTION":
//                    if (!newStatus.equals("UNDER_PRODUCTION")) {
//                        throw new RuntimeException("Only UNDER_PRODUCTION is valid after SENT_FOR_PRODUCTION");
//                    }
//                    break;
//
//                case "UNDER_PRODUCTION":
//                    if (!newStatus.equals("IN_PROCESS")) {
//                        throw new RuntimeException("Only IN_PROCESS is valid after UNDER_PRODUCTION");
//                    }
//                    break;
//
//                case "IN_PROCESS":
//                    if (!newStatus.equals("DISPATCHED")) {
//                        throw new RuntimeException("Only DISPATCHED is valid after IN_PROCESS");
//                    }
//                    break;
//
//                case "DISPATCHED":
//                    if (!newStatus.equals("DELIVERED")) {
//                        throw new RuntimeException("Only DELIVERED is valid after DISPATCHED");
//                    }
//                    break;
//
//                case "DELIVERED":
//                    throw new RuntimeException("Order is already delivered — cannot be updated further.");
//
//                default:
//                    throw new RuntimeException("Invalid status transition for current state: " + currentStatus);
//            }
//
//            order.setPoDeliveryStatus(newStatus);
//        }
//
//        // ----------------------------------------------------
//        // 3️⃣ Expected Delivery Date Update (Optional)
//        // ----------------------------------------------------
//        if (request.getNewExpectedDeliveryDate() != null) {
//            order.setPoExpectedDelivery_date(request.getNewExpectedDeliveryDate());
//        }
//
//        // ----------------------------------------------------
//        // 4️⃣ Item Update (Quantity/Cost Adjustments)
//        // ----------------------------------------------------
//        if (request.getItems() != null) {
//            for (PurchaseOrderUpdateRequest.ItemUpdateRequest itemUpdate : request.getItems()) {
//                PurchaseOrderItem item = purchaseOrderItemRepository.findById(itemUpdate.getItemId())
//                        .orElseThrow(() -> new RuntimeException("Order item not found: " + itemUpdate.getItemId()));
//
//                Product product = item.getProduct();
//                int oldQty = item.getPoiQuantity();
//                Integer newQty = itemUpdate.getNewQuantity();
//
//                if (newQty != null && !newQty.equals(oldQty)) {
//                    int diff = newQty - oldQty;
//                    if (diff > 0) {
//                        if (product.getProductsQuantity() < diff) {
//                            throw new RuntimeException("Insufficient stock for product: " + product.getProductsName());
//                        }
//                        product.setProductsQuantity(product.getProductsQuantity() - diff);
//                    } else if (diff < 0) {
//                        product.setProductsQuantity(product.getProductsQuantity() + Math.abs(diff));
//                    }
//                    item.setPoiQuantity(newQty);
//                    productRepository.save(product);
//                }
//
//                if (itemUpdate.getNewCost() != null) {
//                    item.setPoiCost(itemUpdate.getNewCost());
//                }
//                purchaseOrderItemRepository.save(item);
//            }
//        }
//
//        purchaseOrderRepository.save(order);
//    }

//----------------------------------------------------------------------------------------------------
    
    @Transactional
    public boolean cancelOrder(Long orderId, Principal principal) {
        Optional<PurchaseOrder> optionalOrder = purchaseOrderRepository.findById(orderId);
 
        if (optionalOrder.isPresent()) {
            PurchaseOrder order = optionalOrder.get();
 
            if (!"PENDING".equalsIgnoreCase(order.getPoDeliveryStatus())) {
                throw new RuntimeException("Only PENDING orders can be cancelled.");
            }
 
            List<PurchaseOrderItem> items = purchaseOrderItemRepository.findByPurchaseOrder(order);
            for (PurchaseOrderItem item : items) {
                Product product = item.getProduct();
                int qty = item.getPoiQuantity();
                product.setProductsQuantity(product.getProductsQuantity() + qty);
                productRepository.save(product);
            }
 
            order.setPoDeliveryStatus("CANCELLED");
            purchaseOrderRepository.save(order);
            return true;
        }
 
        return false;
    }
 
//----------------------------------------------------------------------------------------------------
    
    private Long generateId() {
        return System.currentTimeMillis();
    }
 
    private Long generateItemId() {
        return System.nanoTime();
    }
 
    public PurchaseOrder save(PurchaseOrder order) {
        return purchaseOrderRepository.save(order);
    }
	
    public PurchaseOrder getOrderById(Long orderId) {
        return purchaseOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }
    
    public boolean deleteOrderItem(Long itemId) {
        Optional<PurchaseOrderItem> itemOpt = purchaseOrderItemRepository.findById(itemId);
        if (itemOpt.isPresent()) {
            purchaseOrderItemRepository.delete(itemOpt.get());
            return true;
        }
        return false;
    }
}
