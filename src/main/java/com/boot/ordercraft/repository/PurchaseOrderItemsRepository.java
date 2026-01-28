package com.boot.ordercraft.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.boot.ordercraft.model.PurchaseOrder;
import com.boot.ordercraft.model.PurchaseOrderItem;

@Repository
public interface PurchaseOrderItemsRepository extends JpaRepository<PurchaseOrderItem, Long> {

    // 🔹 Fetch all items for a particular purchase order entity
    List<PurchaseOrderItem> findByPurchaseOrder(PurchaseOrder order);

    // 🔹 Fetch all items by purchase order ID (used in payment calculation)
    @Query("SELECT i FROM PurchaseOrderItem i WHERE i.purchaseOrder.poId = :poId")
    List<PurchaseOrderItem> findByPurchaseOrderId(Long poId);

    // 🔹 Calculate total quantity of all items purchased (for reports)
    @Query(value = "SELECT COALESCE(SUM(POIQUANTITY), 0) FROM PURCHASE_ORDER_ITEMS", nativeQuery = true)
    long getTotalPurchasedItems();
    
    @Query("SELECT i.Rawmaterial FROM PurchaseOrderItem i WHERE i.purchaseOrder.poId = :poId AND i.Rawmaterial IS NOT NULL")
    List<Object> findRawMaterialsByPurchaseOrderId(Long poId);
}
