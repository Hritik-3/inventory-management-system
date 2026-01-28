package com.boot.ordercraft.repository;

import com.boot.ordercraft.model.Category;
import com.boot.ordercraft.model.ProductionLine;
import com.boot.ordercraft.model.ProductionUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductionUnitRepository extends JpaRepository<ProductionUnit, Long> {

    // Find all units by exact status
    List<ProductionUnit> findByStatus(String status);

    // Find units by category entity
    List<ProductionUnit> findByCategory(Category category);

    // ✅ Fixed: Find units by category's actual field name
    List<ProductionUnit> findByCategory_CategoriesId(Long categoriesId);

    // Find units by category id and status
    List<ProductionUnit> findByCategory_CategoriesIdAndStatus(Long categoriesId, String status);

    // Optional: find units that have at least one line running
    List<ProductionUnit> findDistinctByProductionLines_Status(String status);
    
}
    
   

