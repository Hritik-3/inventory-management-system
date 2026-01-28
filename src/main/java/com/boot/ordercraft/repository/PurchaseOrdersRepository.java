package com.boot.ordercraft.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import com.boot.ordercraft.model.PurchaseOrder;

@Repository
public interface PurchaseOrdersRepository extends JpaRepository<PurchaseOrder, Long> {

    // 🔹 Fetch all orders placed by a specific user (Procurement Officer)
    List<PurchaseOrder> findByUserUserId(String userId);

    // 🔹 Fetch all purchase orders linked to a specific supplier
    @Query("SELECT po FROM PurchaseOrder po WHERE po.supplier.suppliersId = :supplierId")
    List<PurchaseOrder> findBySupplierId(Long supplierId);

    // 🔹 Fetch all purchase orders with their related customer, items, and product info
    @Query("SELECT DISTINCT po FROM PurchaseOrder po " +
           "JOIN FETCH po.customer " +
           "JOIN FETCH po.items i " +
           "JOIN FETCH i.product")
    List<PurchaseOrder> findAllWithItemsAndCustomer();



         
}


