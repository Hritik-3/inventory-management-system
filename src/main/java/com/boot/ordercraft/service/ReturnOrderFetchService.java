package com.boot.ordercraft.service;

import com.boot.ordercraft.dto.ReturnOrderFetchDTO;
import com.boot.ordercraft.dto.ReturnOrderItemFetchDTO;
import com.boot.ordercraft.model.ReturnOrder;
import com.boot.ordercraft.repository.ReturnOrdersRepository;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReturnOrderFetchService {

    @Autowired
    private ReturnOrdersRepository returnOrderRepository;

    /**
     * Fetch all return orders with their items and map to DTOs
     */
    public List<ReturnOrderFetchDTO> getAllReturnOrders() {
        List<ReturnOrder> returnOrders = returnOrderRepository.findAll();

        return returnOrders.stream().map(ro -> {
            // Map ReturnOrder → ReturnOrderFetchDTO
            ReturnOrderFetchDTO dto = new ReturnOrderFetchDTO();
            dto.setRoId(ro.getRoId());
            dto.setRoReturnDate(ro.getRoReturnDate());
            dto.setRoReturnReason(ro.getRoReturnReason());
            dto.setRoStatus(ro.getRoStatus());

            if (ro.getPurchaseOrder() != null) {
                dto.setPurchaseOrderId(ro.getPurchaseOrder().getPoId());
            }
            if (ro.getReturnedBy() != null) {
                dto.setReturnedByUserId(ro.getReturnedBy().getUserId());
            }

            // ✅ Map each ReturnOrderItem → ReturnOrderItemFetchDTO
            List<ReturnOrderItemFetchDTO> items = ro.getItems().stream().map(item -> {
                ReturnOrderItemFetchDTO itemDTO = new ReturnOrderItemFetchDTO();
                itemDTO.setRoiId(item.getRoiId());
                itemDTO.setReturnQuantity(item.getReturnQuantity());
                itemDTO.setConditionNote(item.getConditionNote());

                if (item.getProduct() != null) {
                    itemDTO.setProductId(item.getProduct().getProductsId());
                    itemDTO.setProductName(item.getProduct().getProductsName());
                }
                if (item.getPurchaseOrderItem() != null) {
                    itemDTO.setPurchaseOrderItemId(item.getPurchaseOrderItem().getPoiId());
                }

                return itemDTO; // ✅ This was missing!
            }).collect(Collectors.toList());

            dto.setItems(items);
            return dto; // ✅ return dto from outer map
        }).collect(Collectors.toList());
    }
}
