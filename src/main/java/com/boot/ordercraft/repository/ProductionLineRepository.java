package com.boot.ordercraft.repository;

import com.boot.ordercraft.model.ProductionLine;
import com.boot.ordercraft.model.ProductionUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductionLineRepository extends JpaRepository<ProductionLine, Long> {

    // Find the first line by status (optional)
    Optional<ProductionLine> findFirstByStatus(String status);

    // Find all lines by status
//    List<ProductionLine> findByStatus(String status);

    // Find all lines for a specific production unit
    List<ProductionLine> findByProductionUnit(ProductionUnit productionUnit);

    // Optional: find lines by unit AND status
    List<ProductionLine> findByProductionUnitAndStatus(ProductionUnit productionUnit, String status);
    
    @Query(value = "SELECT * FROM PRODUCTION_LINE WHERE STATUS = :status AND ROWNUM = 1", nativeQuery = true)
    Optional<ProductionLine> findFirstAvailable(@Param("status") String status);

 // in ProductionLineRepository
    List<ProductionLine> findByStatus(String status);
    
 // fetch only lines belonging to units in the same category
    @Query("SELECT pl FROM ProductionLine pl " +
    	       "WHERE pl.status = :status AND pl.productionUnit.category.id = :categoryId")
    	List<ProductionLine> findAvailableLinesByCategory(@Param("status") String status,
    	                                                  @Param("categoryId") Long categoryId);
    
 // Fetch all lines that are available and belong to a given category
    List<ProductionLine> findByStatusAndProductionUnit_Category_CategoriesId(String status, Long categoryId);
    
 // Fetch all production lines ordered alphabetically by line name
    List<ProductionLine> findAllByOrderByLineNameAsc();




   
    
}

