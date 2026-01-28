package com.boot.ordercraft.repository;
 
import java.util.List;

import java.util.Optional;
 
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;

import org.springframework.stereotype.Repository;
 
import com.boot.ordercraft.model.ReturnOrder;

import com.boot.ordercraft.model.ReturnOrderItem;
 
@Repository

public interface ReturnOrdersRepository extends JpaRepository<ReturnOrder, Long> {
 
	@Query("SELECT DISTINCT ro FROM ReturnOrder ro LEFT JOIN FETCH ro.items")

	List<ReturnOrder> findAllWithDetails();
 
}
 
 