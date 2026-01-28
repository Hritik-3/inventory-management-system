package com.boot.ordercraft.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boot.ordercraft.dto.DispatchResponseDTO;
import com.boot.ordercraft.model.InventoryTransaction;
import com.boot.ordercraft.model.Product;
import com.boot.ordercraft.model.PurchaseOrder;
import com.boot.ordercraft.model.PurchaseOrderItem;
import com.boot.ordercraft.repository.InventoryTransactionRepository;
import com.boot.ordercraft.repository.ProductsRepository;
import com.boot.ordercraft.repository.PurchaseOrdersRepository;

@Service
public class ScheduleService {

    @Autowired
    private PurchaseOrdersRepository purchaseOrderRepo;

    @Autowired
    private ProductsRepository productRepo;

    @Autowired
    private InventoryTransactionRepository inventoryTransactionRepo;

    public DispatchResponseDTO dispatchWholeOrder(Long purchaseOrderId) {
        // 1. Fetch Purchase Order
        PurchaseOrder order = purchaseOrderRepo.findById(purchaseOrderId)
                .orElseThrow(() -> new RuntimeException("Purchase order not found"));

     // 2. Check stock availability for ALL items
        for (PurchaseOrderItem item : order.getItems()) {
            Product product = item.getProduct(); // ✅ directly get product from item

            if (product.getProductsQuantity() < item.getPoiQuantity()) {
                throw new RuntimeException(
                    "Insufficient stock for product: " + product.getProductsName()
                );
            }
        }

     // 3. If all products are available → deduct stock & record transactions
        for (PurchaseOrderItem item : order.getItems()) {
            Product product = item.getProduct(); // ✅ no need to fetch again

            // Deduct stock
            int updatedQty = product.getProductsQuantity() - item.getPoiQuantity();
            product.setProductsQuantity(updatedQty);
            productRepo.save(product);

            // Save transaction
            InventoryTransaction txn = new InventoryTransaction();
            txn.setItId(product.getProductsId());
            txn.setItQuantity(item.getPoiQuantity());
            txn.setItTransactionType("OUT");
            //txn.setPurchaseOrderId(order.getPoId()); // or order.getId() depending on your entity field name
            inventoryTransactionRepo.save(txn);
        }
        
        order.setPoDeliveryStatus("DISPATCHED"); // or if you use enum: order.setStatus(OrderStatus.DISPATCHED);
        purchaseOrderRepo.save(order);


        // 4. Build response
        DispatchResponseDTO response = new DispatchResponseDTO();
        response.setPurchaseOrderId(order.getPoId());
        response.setMessage("Order dispatched successfully");
        response.setDispatchedItems(order.getItems().stream()
                .map(i -> new DispatchResponseDTO.ItemDTO(
                        i.getProduct().getProductsId(),   // ✅ product id from relation
                        i.getPoiQuantity()
                ))
                .toList());

        return response;
    }
}
