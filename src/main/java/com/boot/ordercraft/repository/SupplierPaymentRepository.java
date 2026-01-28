package com.boot.ordercraft.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.boot.ordercraft.model.SupplierPayment;

@Repository
public interface SupplierPaymentRepository extends JpaRepository<SupplierPayment, Long> {
    List<SupplierPayment> findBySupplier_SuppliersId(Long supplierId);
    
    List<SupplierPayment> findByPoId(Long poId);

    @Query("SELECT SUM(p.amountPaid) FROM SupplierPayment p WHERE p.poId = :poId")
    Double findTotalPaidAmountByPoId(Long poId);
    
    @Query(value = """
    	    SELECT r.RWID, r.RWNAME, r.RWCATEGORY, r.RWUNIT, r.RWCOST 
    	    FROM PURCHASE_ORDER_ITEMS i
    	    JOIN RAW_MATERIALS r ON i.POIRAWMATERIALID = r.RWID
    	    WHERE i.POIPURCHASEORDERID = :poId
    	""", nativeQuery = true)
    	List<Object> findRawMaterialsByPurchaseOrderId(Long poId);


}
