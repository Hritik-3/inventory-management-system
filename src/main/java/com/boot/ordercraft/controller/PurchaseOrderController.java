package com.boot.ordercraft.controller;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.boot.ordercraft.dto.ApiResponseDTO;
import com.boot.ordercraft.dto.OrderDetailsDTO;
import com.boot.ordercraft.dto.ProductDTO;
import com.boot.ordercraft.dto.PurchaseOrderCreateRequest;
import com.boot.ordercraft.dto.PurchaseOrderInternalRequest;
import com.boot.ordercraft.dto.PurchaseOrderUpdateRequest;
import com.boot.ordercraft.dto.RawMaterialSearchResponse;
import com.boot.ordercraft.dto.ReturnOrderRequestDTO;
import com.boot.ordercraft.dto.PurchaseOrderDTO;
import com.boot.ordercraft.dto.SupplierDTO;
import com.boot.ordercraft.dto.ViewOrderDTO;
import com.boot.ordercraft.model.Address;
import com.boot.ordercraft.model.Product;
import com.boot.ordercraft.model.PurchaseOrder;
import com.boot.ordercraft.model.RawMaterial;
import com.boot.ordercraft.model.Supplier;
import com.boot.ordercraft.model.User;

import com.boot.ordercraft.repository.ProductsRepository;
import com.boot.ordercraft.repository.PurchaseOrdersRepository;
import com.boot.ordercraft.repository.RawMaterialsRepository;
import com.boot.ordercraft.repository.SuppliersRepository;
import com.boot.ordercraft.repository.UserRepository;

import com.boot.ordercraft.service.PurchaseOrderService;
import com.boot.ordercraft.service.ReturnOrderService;
import com.boot.ordercraft.service.UserService;
import com.boot.ordercraft.service.ViewOrderService;
import com.boot.ordercraft.util.PurchaseOrderMapper;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class PurchaseOrderController {

    @Autowired
    private PurchaseOrdersRepository purchaseOrdersRepository;

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private SuppliersRepository supplierRepository;

    @Autowired
    private ProductsRepository productRepository;
    
    @Autowired
    private ViewOrderService viewOrderService;
    
    @Autowired
    private ReturnOrderService returnOrderService;
    
    @Autowired
    private RawMaterialsRepository rawmaterialRepo;
    

    // --------------------------- Orders ---------------------------

    @PostMapping("/purchase-orders")
    public ResponseEntity<String> createOrder(@RequestBody PurchaseOrderCreateRequest request, Principal principal) {
        String username = principal.getName();   // ✅ take logged in user
        purchaseOrderService.createPurchaseOrder(request, username);
        return ResponseEntity.ok("Purchase Order created successfully");
    }


    @PostMapping("/internal-order")
    public ResponseEntity<String> createInternalOrder(@RequestBody PurchaseOrderInternalRequest request, Principal principal) {
        purchaseOrderService.createInternalOrder(request, principal.getName());
        return ResponseEntity.ok("Internal Purchase Order created successfully");
    }

    @PutMapping("/orders/{orderId}")
    public ResponseEntity<String> updateOrder(@PathVariable Long orderId, @RequestBody PurchaseOrderUpdateRequest request, Principal principal) {
        //purchaseOrderService.updateOrderAndItems(orderId, request, principal.getName());
        return ResponseEntity.ok("Order updated successfully");
    }

//    @PutMapping("/orders2/{orderId}/cancel")
//    public ResponseEntity<String> cancelOrder(@PathVariable Long orderId, Principal principal) {
//        boolean cancelled = purchaseOrderService.cancelOrder(orderId, principal);
//        if (cancelled) {
//            return ResponseEntity.ok("Order cancelled successfully.");
//        } else {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Cannot cancel the order.");
//        }
//    }

//    @GetMapping("/purchase-orders")
//    public ResponseEntity<List<PurchaseOrderDTO>> getAllOrders() {
//        List<PurchaseOrderDTO> dtos = purchaseOrdersRepository.findAll().stream()
//                .map(PurchaseOrderMapper::toDTO)
//                .toList();
//        return ResponseEntity.ok(dtos);
//    }

    @GetMapping("/purchase-orders/{orderId}")
    public ResponseEntity<PurchaseOrderDTO> getOrderById2(@PathVariable Long orderId) {
        return purchaseOrdersRepository.findById(orderId)
                .map(PurchaseOrderMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    
    
//    @GetMapping("/getallorders")
//    public ResponseEntity<List<ViewOrderDTO>> getAllOrders(Principal principal) {
//        String username = principal.getName();
//
//        System.out.println("🔑 principal.getName() = " + username);
//        
//        // ✅ Fetch the User object
//        User user = userRepository.findByEmail(username)
//                .orElseThrow(() -> new RuntimeException("User not found: " + username));
//
//
//        // 🔎 Debug log
//        System.out.println("👤 Logged in user: " + user.getUserName() +
//                           " | Role: " + user.getRole().getRoleName() +
//                           " | UserId: " + user.getUserId());
//
//        // ✅ Pass username (or even userId if your service expects it)
//        List<ViewOrderDTO> orders = viewOrderService.getAllOrders(username);
//
//        return ResponseEntity.ok(orders);
//    }

    
    //new added here
    
   

    @GetMapping("/getallorders")
    public ResponseEntity<List<ViewOrderDTO>> getAllOrders() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getName().equals("anonymousUser")) {
            // Handle unauthenticated case gracefully
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = authentication.getName();
        System.out.println("🔑 Logged in username = " + username);

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        List<ViewOrderDTO> orders = viewOrderService.getAllOrders(username);
        return ResponseEntity.ok(orders);
    }



    @PostMapping("/return-orders")
    public ResponseEntity<ApiResponseDTO> submitReturnOrder(
            @RequestBody ReturnOrderRequestDTO dto) {
    	System.out.println("RETURN API HIT");
        return returnOrderService.createReturnOrder(dto);
    }
 
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<OrderDetailsDTO> getOrderById(@PathVariable Long orderId) {
        PurchaseOrder order = purchaseOrderService.getOrderById(orderId);
 
        OrderDetailsDTO dto = new OrderDetailsDTO();
        dto.setPoId(order.getPoId());
        dto.setCustomerName(order.getCustomer() != null ? order.getCustomer().getName() : "N/A");
        dto.setDeliveryStatus(order.getPoDeliveryStatus());
        dto.setExpectedDeliveryDate(order.getPoExpectedDelivery_date());
 
        List<OrderDetailsDTO.ItemDTO> items = order.getItems().stream().map(item -> {
            OrderDetailsDTO.ItemDTO itemDTO = new OrderDetailsDTO.ItemDTO();
            itemDTO.setItemId(item.getPoiId());
            itemDTO.setProductName(item.getProduct() != null ? item.getProduct().getProductsName() : "Unknown");
            itemDTO.setQuantity(item.getPoiQuantity());
            itemDTO.setUnitPrice(item.getProduct() != null ? item.getProduct().getProductsUnitPrice() : 0.0);
//            itemDTO.setReturnedQuantity(
//                item.getReturnOrderItems() != null
//                ? item.getReturnOrderItems().stream().mapToInt(r -> r.getRoiQuantity()).sum()
//                : 0
//            );
            return itemDTO;
        }).toList();
 
        dto.setItems(items);
        return ResponseEntity.ok(dto);
    }
 
 
    // ✅ Update order
//    @PutMapping("/orders/{orderId}")
//    public ResponseEntity<String> updateOrder(
//            @PathVariable Long orderId,
//            @RequestBody PurchaseOrderUpdateRequest request,
//            String Username) {
// 
//        purchaseOrderService.updateOrderAndItems(orderId, request, Username);
//        return ResponseEntity.ok("Order updated successfully");
//    }
 
    // ✅ Cancel order
    @PutMapping("/orders/{orderId}/cancel")
    public ResponseEntity<String> cancelOrder(
            @PathVariable Long orderId,
            Principal principal) {
 
        boolean cancelled = purchaseOrderService.cancelOrder(orderId, principal);
        if (cancelled) {
            return ResponseEntity.ok("Order cancelled successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Cannot cancel the order.");
        }
    }
    
    
    
    @DeleteMapping("/order-items/{itemId}")
    public ResponseEntity<String> deleteOrderItem(@PathVariable Long itemId) {
        boolean deleted = purchaseOrderService.deleteOrderItem(itemId);
        if (deleted) {
            return ResponseEntity.ok("Item deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found");
        }
    }
 
 

    // --------------------------- Products ---------------------------

//    @GetMapping("/products")
//    public List<ProductDTO> getAllProducts() {
//        return productRepository.findAll().stream()
//                .map(p -> new ProductDTO(p.getProductsId(), p.getProductsName(), p.getProductsUnitPrice(), p.getProductsQuantity()))
//                .toList();
//    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductDTO>> searchProducts(@RequestParam String type, @RequestParam String query) {
        List<ProductDTO> productDTOs;
        try {
            if ("id".equalsIgnoreCase(type)) {
                Long id = Long.parseLong(query);
                productDTOs = productRepository.findByProductsId(id).stream()
                        .map(p -> new ProductDTO(p.getProductsId(), p.getProductsName(), p.getProductsUnitPrice(), p.getProductsQuantity()))
                        .toList();
            } else {
                productDTOs = productRepository.searchByProductsNameContainingIgnoreCase(query).stream()
                        .map(p -> new ProductDTO(p.getProductsId(), p.getProductsName(), p.getProductsUnitPrice(), p.getProductsQuantity()))
                        .toList();
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }

        return ResponseEntity.ok(productDTOs);
    }

    // --------------------------- Suppliers ---------------------------

    @GetMapping("/suppliers2")
    public List<SupplierDTO> getAllSupplier() {
        return supplierRepository.findAll().stream()
                .map(s -> new SupplierDTO(s.getSuppliersId(), s.getSuppliersName()))
                .toList();
    }

    // --------------------------- Customers ---------------------------

    @GetMapping("/{customerId}")
    public ResponseEntity<User> getCustomerDetails(@PathVariable String customerId) {
        return userRepository.findById(customerId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{customerId}/address")
    public ResponseEntity<User> updateCustomerAddress(@PathVariable String customerId, @RequestBody Map<String, Address> body) {
        return userRepository.findById(customerId)
                .map(customer -> {
                    customer.setAddress(body.get("address"));
                    return ResponseEntity.ok(userRepository.save(customer));
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    ///////////////////////////////////////////////////////////////
//  //To Get suppliers
  @GetMapping("/suppliers")
  public List<Supplier> getAllSuppliers() {
      return supplierRepository.findAll().stream()
              .map(s -> new Supplier(s.getSuppliersId(), s.getSuppliersName(),s.getSuppliersPhone(),s.getSuppliersEmail(),s.getSuppliersContactPerson(),s.getAddress()))
              .toList();
  }
  
  
  // 🔹 Get supplier by ID
  @GetMapping("/suppliers/{id}")
  public ResponseEntity<Supplier> getSupplierById(@PathVariable Long id) {
      return supplierRepository.findById(id)
              .map(ResponseEntity::ok)
              .orElse(ResponseEntity.notFound().build());
  }
  

/////////////////////////////////////////////////
    
    // 🔹 Fetch raw materials for a supplier
    @GetMapping("/raw-materials/supplier/{id}")
    public ResponseEntity<List<RawMaterial>> getRawMaterialsBySuppliers(@PathVariable Long id) {
        List<RawMaterial> materials = rawmaterialRepo.findBySupplier_SuppliersId(id);
        return ResponseEntity.ok(materials);
    }
    
    
    //To fetch all RawMaterials
  @GetMapping("/raw-materials")
  public Iterable<RawMaterial> getallRawMaterials() {
  	return rawmaterialRepo.findAll();
  	
  }
    
 
    
    
//    @GetMapping("/raw-materials/search")
//    public List<RawMaterial> searchRawMaterials(@RequestParam String query) {
//        return rawmaterialRepo.findByRwNameContainingIgnoreCase(query);
//    }
 
    
    @GetMapping("/raw-materials/search")
    public List<RawMaterialSearchResponse> searchRawMaterials(@RequestParam String query) {
        return rawmaterialRepo.findByRwNameContainingIgnoreCase(query)
                .stream()
                .map(RawMaterialSearchResponse::new)
                .collect(Collectors.toList());
    }
 
    
    
    @GetMapping("/raw-material/{id}/suppliers")
    public List<Supplier> getSuppliersByRawMaterial(@PathVariable Long id) {
        RawMaterial rm = rawmaterialRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Material not found"));
        return List.of(rm.getSupplier());  // Single supplier for now
    }
 
    
    @PostMapping("/suppliers/{supplierId}/orders")
    public ResponseEntity<String> createOrderForSupplier(
            @PathVariable Long supplierId,
            @RequestBody PurchaseOrderCreateRequest request) {

        // ✅ hardcode a default username OR pass from request
        String username = request.getCreatedBy() != null ? request.getCreatedBy() : "system";

        return purchaseOrderService.createPurchaseOrder(request, username);
    }

 
 
 
 
    
    
}
