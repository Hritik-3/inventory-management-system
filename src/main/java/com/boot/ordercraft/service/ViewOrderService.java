package com.boot.ordercraft.service;

import com.boot.ordercraft.dto.ViewOrderDTO;
import com.boot.ordercraft.dto.ViewOrderDTO.ItemInfo;
import com.boot.ordercraft.model.PurchaseOrder;
import com.boot.ordercraft.model.PurchaseOrderItem;
import com.boot.ordercraft.model.User;
import com.boot.ordercraft.repository.PurchaseOrdersRepository;
import com.boot.ordercraft.repository.ReturnOrderItemsRepository;
import com.boot.ordercraft.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ViewOrderService {

    @Autowired
    private PurchaseOrdersRepository purchaseOrderRepository;

    @Autowired
    private ReturnOrderItemsRepository returnOrderItemRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public List<ViewOrderDTO> getAllOrders(String username) {
        Optional<User> userOpt = userRepository.findByUserName(username);
        if (userOpt.isEmpty()) {
            userOpt = userRepository.findByEmail(username);
        }

        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found: " + username);
        }

        User user = userOpt.get();

        List<PurchaseOrder> orders;

        if ("ADMIN".equalsIgnoreCase(user.getRole().getRoleName())) {
            orders = purchaseOrderRepository.findAllWithItemsAndCustomer()
                    .stream()
                    .filter(po -> "CUSTOMER_ORDER".equalsIgnoreCase(po.getPoOrderType()))
                    .toList();
        } else {
            orders = purchaseOrderRepository.findByUserUserId(user.getUserId())
                    .stream()
                    .filter(po -> "CUSTOMER_ORDER".equalsIgnoreCase(po.getPoOrderType()))
                    .toList();
        }

        return orders.stream().map(po -> {
            ViewOrderDTO dto = new ViewOrderDTO();
            dto.setPoId(po.getPoId());
            dto.setOrderDate(po.getPoOrderDate());
            dto.setExpectedDeliveryDate(po.getPoExpectedDelivery_date());
            dto.setDeliveryStatus(po.getPoDeliveryStatus());

            if (po.getCustomer() == null) {
                System.err.println("⚠️ WARNING: PurchaseOrder with ID " + po.getPoId() + " has null customer.");
                dto.setCustomerName("Unknown Customer");
            } else {
                dto.setCustomerName(po.getCustomer().getName());
            }

            List<ViewOrderDTO.ItemInfo> items = new ArrayList<>();
            double totalAmount = 0.0;

            for (PurchaseOrderItem item : po.getItems()) {
                ViewOrderDTO.ItemInfo itemInfo = new ViewOrderDTO.ItemInfo();
                itemInfo.setItemId(item.getPoiId());
                itemInfo.setProductId(item.getProduct().getProductsId());
                itemInfo.setProductName(item.getProduct().getProductsName());
                itemInfo.setQuantity(item.getPoiQuantity());

                Integer returnedQty = returnOrderItemRepository.sumReturnedQuantityByPurchaseOrderItemId(item.getPoiId());
                itemInfo.setReturnedQuantity(returnedQty != null ? returnedQty : 0);

                items.add(itemInfo);
                totalAmount += item.getPoiQuantity() * item.getProduct().getProductsUnitPrice();

                System.out.println("ℹ️ Returned quantity for POI " + item.getPoiId() + ": " + (returnedQty != null ? returnedQty : 0));
            }

            dto.setItems(items);
            dto.setTotalAmount(totalAmount);

            return dto;
        }).toList();
    }
}
