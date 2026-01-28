package com.boot.ordercraft.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.boot.ordercraft.model.Product;
import com.boot.ordercraft.model.User;

@Repository
public interface ProductsRepository extends JpaRepository<Product, Long> {
	
	
	// Search by product name
	@Query("SELECT p FROM Product p WHERE LOWER(p.productsName) LIKE LOWER(CONCAT('%', :name, '%'))")
	List<Product> searchByName(@Param("name") String name);

    // If you want search by exact id
	@Query("SELECT new com.boot.ordercraft.dto.ProductDTO(p.productsId, p.productsName, p.productsUnitPrice, p.productsQuantity) FROM Product p")
    List<Product> findByProductsId(Long id);

    List<Product> searchByProductsNameContainingIgnoreCase(String name);
    
    @Query("SELECT p FROM Product p WHERE p.productsQuantity <= p.minStockThreshold")
    List<Product> findLowStockProducts();
    
// // 🔍 Search by product name (case-insensitive contains)
//    @Query("SELECT p FROM Product p WHERE LOWER(p.productsName) LIKE LOWER(CONCAT('%', :name, '%'))")
//    List<Product> searchByName(@Param("name") String name);
 
//    // 🔍 Search by exact ID
//    List<Product> findByProductsId(Long id);
 
//    // 🔍 Search by name (Spring Data convention)
//    List<Product> searchByProductsNameContainingIgnoreCase(String name);
 
    // 🚨 Find products below min threshold
    @Query("SELECT p FROM Product p WHERE p.productsQuantity < p.minStockThreshold")
    List<Product> findProductsBelowThreshold();
 
    // 🚨 Find products above max threshold
    @Query("SELECT p FROM Product p WHERE p.productsQuantity > p.maxStockThreshold")
    List<Product> findProductsAboveThreshold();
 
    // 🚨 Find products below threshold within a category
    @Query("SELECT p FROM Product p WHERE p.productsQuantity < p.minStockThreshold AND p.category.categoriesId = :categoryId")
    List<Product> findProductsBelowThresholdByCategory(@Param("categoryId") Long categoryId);
 
    // 🔍 Find products by category
    List<Product> findByCategoryCategoriesId(Long categoryId);
 
    // 🚨 Low stock but no upcoming production order
    @Query("""
        SELECT p FROM Product p
        WHERE p.productsQuantity < p.minStockThreshold
        AND NOT EXISTS (
            SELECT ps FROM ProductionSchedule ps
            WHERE ps.product = p
            AND ps.psStartDate > CURRENT_DATE
        )
    """)
    List<Product> findLowStockWithoutUpcomingProduction();
 
    // 🚨 Auto-triggered alerts (first-time) ❌ COMMENTED because autoAlertTimestamp is not in Product.java
//    @Query("""
//        SELECT p FROM Product p
//        WHERE (p.productsQuantity < p.minStockThreshold OR p.productsQuantity > p.maxStockThreshold)
//        AND p.autoAlertTimestamp IS NULL
//    """)
//    List<Product> findAutoTriggeredAlerts();
 
    // ✅ Products recovered back within thresholds ❌ COMMENTED because autoAlertTimestamp is not in Product.java
//    @Query("""
//        SELECT p FROM Product p
//        WHERE p.productsQuantity BETWEEN p.minStockThreshold AND p.maxStockThreshold
//        AND p.autoAlertTimestamp IS NOT NULL
//    """)
//    List<Product> findRecoveredProducts();
 
    // 🚨 Find products with quantity below minimum threshold
//    @Query("SELECT p FROM Product p WHERE p.productsQuantity < p.minStockThreshold")
//    List<Product> findLowStockProducts();

 
}
