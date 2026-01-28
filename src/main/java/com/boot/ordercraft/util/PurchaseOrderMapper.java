package com.boot.ordercraft.util;

import com.boot.ordercraft.model.PurchaseOrder;

import com.boot.ordercraft.model.PurchaseOrderItem;

import com.boot.ordercraft.dto.*;
 
import java.util.List;

import java.util.stream.Collectors;
 
public class PurchaseOrderMapper {
 
    public static PurchaseOrderDTO toDTO(PurchaseOrder order) {

        PurchaseOrderDTO dto = new PurchaseOrderDTO();

        dto.setPoId(order.getPoId());

        dto.setPoOrderDate(order.getPoOrderDate());

        dto.setPoExpectedDeliveryDate(order.getPoExpectedDelivery_date());

        dto.setPoDeliveryStatus(order.getPoDeliveryStatus());
 
        if (order.getSupplier() != null) {

            dto.setSupplier(new SupplierDTO(order.getSupplier().getSuppliersId(), order.getSupplier().getSuppliersName()));

        }
 
        if (order.getUser() != null) {

            dto.setUser(UserDto.fromEntity(order.getUser()));

        }
 
        if (order.getItems() != null) {

            List<PurchaseOrderItemDTO> itemsDto = order.getItems().stream()

                .map(item -> {

                    PurchaseOrderItemDTO itemDto = new PurchaseOrderItemDTO();

                    itemDto.setItemId(item.getPoiId()); // adjust if id field name differs

                    itemDto.setQuantity(item.getPoiQuantity());

                    itemDto.setCost(item.getPoiCost());
 
                    if (item.getProduct() != null) {

                        ProductDTO productDto = new ProductDTO(
                            item.getProduct().getProductsId(),
                            item.getProduct().getProductsName(),
                            item.getProduct().getProductsUnitPrice(),
                            item.getProduct().getProductsQuantity()
                        );

                        itemDto.setProduct(productDto);

                    }
 
                    return itemDto;

                }).collect(Collectors.toList());
 
            dto.setItems(itemsDto);

        }
 
        return dto;

    }

}

 