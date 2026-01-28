package com.boot.ordercraft.repository;

import com.boot.ordercraft.model.Product;
import com.boot.ordercraft.model.ProductionSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ProductionScheduleRepository extends JpaRepository<ProductionSchedule, Long> {

    // ✅ Find schedules by status (case-sensitive)
    List<ProductionSchedule> findByPsStatus(String psStatus);

    // ✅ Find schedules by status (case-insensitive)
    List<ProductionSchedule> findByPsStatusIgnoreCase(String psStatus);

    // ✅ Check if a product already has a schedule with a given status (case-sensitive)
    @Query("select case when count(ps) > 0 then true else false end " +
    	       "from ProductionSchedule ps " +
    	       "where ps.product = :product and ps.psStatus = :status")
    	boolean existsByProductAndPsStatus(@Param("product") Product product,
    	                                   @Param("status") String psStatus);


    // ✅ Check if a product already has a schedule with a given status (case-insensitive)
    @Query("select count(ps) > 0 from ProductionSchedule ps " +
           "where ps.product = :product and upper(ps.psStatus) = upper(:psStatus)")
    boolean existsByProductAndPsStatusIgnoreCase(Product product, String psStatus);
    
    // 🔹 Filter schedules by category, status, and date range
    @Query("SELECT ps FROM ProductionSchedule ps " +
           "WHERE (:categoryId IS NULL OR ps.product.category.categoriesId = :categoryId) " +
           "AND (:status IS NULL OR ps.psStatus = :status) " +
           "AND (:startDate IS NULL OR ps.psStartDate >= :startDate) " +
           "AND (:endDate IS NULL OR ps.psEndDate <= :endDate)")
    List<ProductionSchedule> findByFilters(
            @Param("categoryId") Long categoryId,
            @Param("status") String status,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
 
 
    
    
 // ✅ Get schedules in descending order of psId
    List<ProductionSchedule> findAllByOrderByPsIdDesc();
       
}
