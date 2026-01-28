package com.boot.ordercraft.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.boot.ordercraft.model.ReturnOrderItem;

@Repository
public interface ReturnOrderItemsRepository extends JpaRepository<ReturnOrderItem, Long> {

    // Get one ReturnOrderItem by PurchaseOrderItem ID (first row)
    @Query(value = "SELECT * FROM RETURN_ORDER_ITEMS WHERE ROI_POI_ID = :poiId AND ROWNUM = 1", nativeQuery = true)
    Optional<ReturnOrderItem> findByPoiId(@Param("poiId") Long poiId);

    // Sum returned quantity for a specific PurchaseOrderItem
    @Query("SELECT COALESCE(SUM(roi.returnQuantity), 0) FROM ReturnOrderItem roi WHERE roi.purchaseOrderItem.id = :poiId")
    Integer sumReturnedQuantityByPurchaseOrderItemId(@Param("poiId") Long purchaseOrderItemId);

    // Total returned items
    @Query("SELECT COALESCE(SUM(r.returnQuantity), 0) FROM ReturnOrderItem r")
    long getTotalReturnedItems();

    // Returned items grouped by month (JPQL version)

    @Query("SELECT FUNCTION('TO_CHAR', r.returnOrder.roReturnDate, 'YYYY-MM') as month, " +
           "SUM(r.returnQuantity) " +
           "FROM ReturnOrderItem r " +
           "GROUP BY FUNCTION('TO_CHAR', r.returnOrder.roReturnDate, 'YYYY-MM') " +
           "ORDER BY month")
    List<Object[]> getReturnedItemsByMonth();
    
   



}
