package com.boot.ordercraft.service;
 
import com.boot.ordercraft.dto.InvoiceResponseDTO;
import com.boot.ordercraft.dto.OrderItemDTO;
import com.boot.ordercraft.model.Address;
import com.boot.ordercraft.model.Customer;
import com.boot.ordercraft.model.PurchaseOrder;
import com.boot.ordercraft.model.User;
import com.boot.ordercraft.repository.PurchaseOrdersRepository;
import org.springframework.stereotype.Service;
 
import java.util.List;
import java.util.stream.Collectors;
 
@Service
public class InvoiceServiceImpl implements InvoiceService {
 
    private final PurchaseOrdersRepository purchaseOrdersRepository;
 
    // ✅ Constructor-based dependency injection (best practice)
    public InvoiceServiceImpl(PurchaseOrdersRepository purchaseOrdersRepository) {
        this.purchaseOrdersRepository = purchaseOrdersRepository;
    }
 
    @Override
    public InvoiceResponseDTO generateInvoice(Long orderId) {
        PurchaseOrder order = purchaseOrdersRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
 
        User user = order.getUser();
        if (user == null) {
            throw new RuntimeException("No user associated with this order.");
        }
        
        Customer customer = order.getCustomer();
        if (customer == null) {
            throw new RuntimeException("No customer associated with this order.");
        }
        
        
 
        String address = customer.getAddress();
        if (address == null) {
            throw new RuntimeException("Customer address not found.");
        }
 
        List<OrderItemDTO> items = order.getItems().stream()
                .map(item -> new OrderItemDTO(
                        item.getProduct() != null ? item.getProduct().getProductsName() : "Unknown Product",
                        item.getProduct() != null && item.getProduct().getCategory() != null
                                ? item.getProduct().getCategory().getCategoryName()
                                : "Unknown Category",
                        item.getPoiQuantity(),
                        item.getPoiCost()
                ))
                .collect(Collectors.toList());
 
//        String fullAddress = String.join(", ",
//                safe(address.getAddressStreet()),
//                safe(address.getAddressCity()),
//                safe(address.getAddressState()),
//                safe(address.getAddressPostalCode()),
//                safe(address.getAddressCountry())
//        );
        
        
 
        // ✅ Calculate subtotal
        double subtotal = items.stream()
                .mapToDouble(item -> item.getUnitPrice() * item.getQuantity())
                .sum();
 
        // ✅ Calculate tax and total
        double tax = subtotal * 0.18;
        double total = subtotal + tax;
 
        // ✅ Debug log
        System.out.println("Subtotal: ₹" + subtotal);
        System.out.println("Tax (18%): ₹" + tax);
        System.out.println("Total Price: ₹" + total);
 
        // ✅ Create response
        InvoiceResponseDTO dto = new InvoiceResponseDTO(
                order.getPoId(),
                customer.getId(),
                customer.getName(),
                address,
                safe(customer.getPhone()),
                items
        );
        dto.setSubtotal(subtotal);
        dto.setTax(tax);
        dto.setTotalPrice(total);
 
        return dto;
    }
 
    private String safe(String value) {
        return value == null ? "" : value;
    }
}
 
 