package com.boot.ordercraft.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.boot.ordercraft.dto.PurchaseOrderInventoryManagerDTO;
import com.boot.ordercraft.dto.PurchaseOrderViewDTO;
import com.boot.ordercraft.model.PurchaseOrder;
import com.boot.ordercraft.repository.PurchaseOrdersRepository;

@Service
public class InventoryManagerService {

    private final PurchaseOrdersRepository purchaseOrdersRepository;

    public InventoryManagerService(PurchaseOrdersRepository purchaseOrdersRepository) {
        this.purchaseOrdersRepository = purchaseOrdersRepository;
    }

    /** ✅ Fetch ALL orders (no filters) */
    public List<PurchaseOrderViewDTO> getAllOrdersWithItems() {
        List<PurchaseOrder> orders = purchaseOrdersRepository.findAllWithItemsAndCustomer();

        return orders.stream().map(this::convertToDto).toList();
    }

    private PurchaseOrderViewDTO convertToDto(PurchaseOrder order) {
        PurchaseOrderViewDTO dto = new PurchaseOrderViewDTO();
        dto.setPoId(order.getPoId());
        dto.setPoDeliveryStatus(order.getPoDeliveryStatus());
        dto.setPoOrderDate(order.getPoOrderDate());
        dto.setPoExpectedDelivery_date(order.getPoExpectedDelivery_date());
        dto.setPoOrderType(order.getPoOrderType());

        if (order.getCustomer() != null) {
            dto.setCustomerName(order.getCustomer().getName());
        }

        if (order.getSupplier() != null) {
            dto.setSupplierName(order.getSupplier().getSuppliersName());
        }

        // Convert items list
        List<PurchaseOrderInventoryManagerDTO> itemDtos = order.getItems().stream().map(item -> {
            String productName = (item.getProduct() != null) ? item.getProduct().getProductsName() : null;
            Long productId = (item.getProduct() != null) ? item.getProduct().getProductsId() : null;

            return new PurchaseOrderInventoryManagerDTO(
                item.getPoiId(),
                item.getPoiQuantity(),
                item.getPoiCost(),
                productName,
                productId
            );
        }).toList();

        dto.setItems(itemDtos);
        return dto;
    }
}
