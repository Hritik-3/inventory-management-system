package com.boot.ordercraft.repository;
 
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;

import java.util.List;
 
import com.boot.ordercraft.model.SupplierRating;
 
public interface SupplierRatingRepository extends JpaRepository<SupplierRating, Long> {
 
    // Get all ratings for a supplier

    List<SupplierRating> findBySupplier_SuppliersId(Long supplierId);
 
    // ✅ FIX: Oracle compatible query using ROWNUM instead of FETCH FIRST

    @Query(value = """

        SELECT * FROM (

            SELECT sr.*

            FROM SUPPLIER_RATING sr

            WHERE sr.SUPPLIER_ID = :supplierId

            ORDER BY sr.RATING_DATE DESC

        )

        WHERE ROWNUM = 1

        """, nativeQuery = true)

    SupplierRating findLatestBySupplierId(@Param("supplierId") Long supplierId);

}

 