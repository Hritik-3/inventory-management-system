package com.boot.ordercraft.repository;

import com.boot.ordercraft.model.InventoryTransaction;

import com.boot.ordercraft.model.Product;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface InventoryTransactionRepository extends JpaRepository<InventoryTransaction, Long> {

    List<InventoryTransaction> findByProduct(Product product);

}
 