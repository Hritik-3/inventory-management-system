package com.boot.ordercraft.model;
 
import com.fasterxml.jackson.annotation.JsonIgnore;
 
import jakarta.persistence.*;
 
@Entity

@Table(name = "RETURN_ORDER_ITEMS")

public class ReturnOrderItem {
 
    @Id

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "return_order_items_seq")
    @SequenceGenerator(
        name = "return_order_items_seq",
        sequenceName = "RETURN_ORDER_ITEMS_SEQ",
        allocationSize = 1,
        initialValue = 1
    )

    @Column(name = "ROI_ID")

    private Long roiId;
 
    @Column(name = "ROI_QUANTITY")

    private Integer returnQuantity;
 
    @Column(name = "ROI_CONDITION_NOTE")

    private String conditionNote;
 
    @ManyToOne
    @JoinColumn(name = "ROI_RETURN_ORDER_ID")
    @JsonIgnore
    private ReturnOrder returnOrder;
 
    @ManyToOne

    @JoinColumn(name = "ROI_PRODUCT_ID")

    private Product product;

    @ManyToOne

    @JoinColumn(name = "ROI_POI_ID")

    private PurchaseOrderItem purchaseOrderItem;

 
    public PurchaseOrderItem getPurchaseOrderItem() {

		return purchaseOrderItem;

	}
 
	public void setPurchaseOrderItem(PurchaseOrderItem purchaseOrderItem) {

		this.purchaseOrderItem = purchaseOrderItem;

	}
 
	// Getters and Setters

    public Long getRoiId() {

        return roiId;

    }
 
    public void setRoiId(Long roiId) {

        this.roiId = roiId;

    }
 
    public Integer getReturnQuantity() {

        return returnQuantity;

    }
 
    public void setReturnQuantity(Integer returnQuantity) {

        this.returnQuantity = returnQuantity;

    }
 
    public String getConditionNote() {

        return conditionNote;

    }
 
    public void setConditionNote(String conditionNote) {

        this.conditionNote = conditionNote;

    }
 
    public ReturnOrder getReturnOrder() {

        return returnOrder;

    }
 
    public void setReturnOrder(ReturnOrder returnOrder) {

        this.returnOrder = returnOrder;

    }
 
    public Product getProduct() {

        return product;

    }
 
    public void setProduct(Product product) {

        this.product = product;

    }

}
 
 