package com.boot.ordercraft.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;

import com.boot.ordercraft.model.LowStockAlert;

import java.util.List;
 
public interface LowStockAlertRepository extends JpaRepository<LowStockAlert, Long> {

    List<LowStockAlert> findByStatus(String status);

    @Query("SELECT CASE WHEN COUNT(lsa) > 0 THEN true ELSE false END " +

    	       "FROM LowStockAlert lsa " +

    	       "WHERE lsa.productId = :productId AND lsa.status = :status")

    	boolean existsByProductIdAndStatus(@Param("productId") Long productId,

    	                                   @Param("status") String status);

    boolean existsByProductId(Long productId);
 
 
}
 