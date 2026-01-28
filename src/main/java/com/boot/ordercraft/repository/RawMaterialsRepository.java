package com.boot.ordercraft.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.boot.ordercraft.model.RawMaterial;

@Repository
public interface RawMaterialsRepository extends JpaRepository<RawMaterial, Long> {
	
	
	
	List<RawMaterial> findBySupplierSuppliersId(Long supplierId);
	
	
	 // Search by raw material name (case-insensitive)
   List<RawMaterial> findByRwNameContainingIgnoreCase(String rwName);
	
	
	 List<RawMaterial> findBySupplier_SuppliersId(Long supplierId);
	 
	 @Query(value = """
		        SELECT MIN(floor(SUM(r.RWQUANTITY) / prm.REQUIRED_QTY)) AS possible_units
		        FROM PRODUCT_RAW_MATERIAL prm
		        JOIN RAW_MATERIAL r ON r.RWID = prm.RWID
		        WHERE prm.PRODUCT_ID = :productId
		        """, nativeQuery = true)
		    Integer getPossibleProductionUnitsByProduct(@Param("productId") Long productId);

		    /**
		     * Fallback (quick check): Sum all raw-material quantity in table.
		     * Use this only if you don't have product->raw material mapping yet.
		     * This returns total raw quantity (not product-specific).
		     */
		    @Query(value = "SELECT NVL(SUM(r.RWQUANTITY),0) FROM RAW_MATERIAL r", nativeQuery = true)
		    Integer getTotalRawMaterialQuantity();

}
