//testing for updated code

package com.boot.ordercraft.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.boot.ordercraft.model.PurchaseOrder;
import com.boot.ordercraft.model.PurchaseOrderItem;
import com.boot.ordercraft.model.Supplier;
import com.boot.ordercraft.model.SupplierPayment;
import com.boot.ordercraft.repository.PurchaseOrderItemsRepository;
import com.boot.ordercraft.repository.PurchaseOrdersRepository;
import com.boot.ordercraft.repository.SupplierPaymentRepository;
import com.boot.ordercraft.repository.SuppliersRepository;

@Service
public class SupplierPaymentService {

    @Autowired
    private SupplierPaymentRepository paymentRepo;

    @Autowired
    private SuppliersRepository supplierRepo;

    @Autowired
    private PurchaseOrdersRepository orderRepo;

    @Autowired
    private PurchaseOrderItemsRepository itemRepo;

    /**
     * ✅ Save a payment manually (with supplier explicitly provided)
     */
    public Object savePayment(SupplierPayment payment) {
        if (payment.getPoId() != null) {
            // Auto payment flow (generate refNo + check duplicates)
            return createPaymentFromPo(
                payment.getPoId(),
                payment.getPaymentMode(),
                payment.getPaymentStatus(),
                payment.getRemarks()
            );
        }

        if (payment.getSupplier() != null && payment.getSupplier().getSuppliersId() != null) {
            Optional<Supplier> supplierOpt = supplierRepo.findById(payment.getSupplier().getSuppliersId());
            if (supplierOpt.isPresent()) {
                payment.setSupplier(supplierOpt.get());
            } else {
                throw new RuntimeException("Supplier not found with ID: " + payment.getSupplier().getSuppliersId());
            }
        } else {
            throw new RuntimeException("Supplier ID must be provided when creating a payment.");
        }

        return paymentRepo.save(payment);
    }

    /**
     * ✅ Automatically create a payment from a Purchase Order (PO_ID)
     * Prevents duplicate/full payments and returns raw materials as part of response
     */
    public Map<String, Object> createPaymentFromPo(Long poId, String paymentMode,
                                                   String paymentStatus, String remarks) {

        // 1️⃣ Find the order
        PurchaseOrder order = orderRepo.findById(poId)
            .orElseThrow(() -> new RuntimeException("Purchase Order not found with ID: " + poId));

        // 2️⃣ Get supplier
        Supplier supplier = order.getSupplier();
        if (supplier == null) {
            throw new RuntimeException("No supplier linked to Purchase Order ID: " + poId);
        }

        // 3️⃣ Fetch order items
        List<PurchaseOrderItem> items = itemRepo.findByPurchaseOrderId(poId);
        if (items.isEmpty()) {
            throw new RuntimeException("No raw materials found for Purchase Order ID: " + poId);
        }

        // 4️⃣ Calculate total PO amount
        double totalAmount = items.stream()
            .mapToDouble(item -> item.getPoiCost() * item.getPoiQuantity())
            .sum();

        // 5️⃣ Get total paid so far
        Double totalPaid = paymentRepo.findTotalPaidAmountByPoId(poId);
        if (totalPaid == null) totalPaid = 0.0;

        double remainingAmount = totalAmount - totalPaid;
        if (remainingAmount <= 0) {
            throw new RuntimeException("Full payment already made for Purchase Order ID: " + poId);
        }

        // 6️⃣ Generate reference number using first raw material
        String rawMaterialName = items.get(0).getRawmaterial().getRwName();
        String referenceNo = generateReferenceNo(rawMaterialName, supplier.getSuppliersId());

        // 7️⃣ Create and save payment
        SupplierPayment payment = new SupplierPayment();
        payment.setSupplier(supplier);
        payment.setPoId(poId);
        payment.setAmountPaid(remainingAmount);
        payment.setPaymentMode(paymentMode);
        payment.setPaymentStatus(paymentStatus);
        payment.setReferenceNo(referenceNo);
        payment.setRemarks(remarks);

        SupplierPayment savedPayment = paymentRepo.save(payment);

        // 8️⃣ Fetch associated raw materials
        List<Object> rawMaterials = itemRepo.findRawMaterialsByPurchaseOrderId(poId);

        // 9️⃣ Prepare response
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Payment successfully recorded");
        response.put("payment", savedPayment);
        response.put("rawMaterials", rawMaterials);

        return response;
    }

    /**
     * ✅ Generate Reference Number using Raw Material + Supplier ID
     */
    private String generateReferenceNo(String rawMaterialName, Long supplierId) {
        String prefix = rawMaterialName.replaceAll("\\s+", "").toUpperCase();
        prefix = prefix.length() > 5 ? prefix.substring(0, 5) : prefix;

        String datePart = java.time.LocalDate.now().toString().replace("-", ""); // e.g., 20251104

        return "TXN-" + prefix + supplierId + "-" + datePart;
    }

    /**
     * ✅ Get raw materials associated with a PO
     */
    public List<Object> getRawMaterialsForPo(Long poId) {
        return itemRepo.findRawMaterialsByPurchaseOrderId(poId);
    }

    /**
     * ✅ Get all payments for a specific supplier
     */
    public List<SupplierPayment> getPaymentsBySupplier(Long supplierId) {
        return paymentRepo.findBySupplier_SuppliersId(supplierId);
    }

    /**
     * ✅ Get all payments (for tracking / admin)
     */
    public List<SupplierPayment> getAllPayments() {
        return paymentRepo.findAll();
    }
}
