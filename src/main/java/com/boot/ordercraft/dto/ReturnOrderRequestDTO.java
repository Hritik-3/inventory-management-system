package com.boot.ordercraft.dto;
 
import java.util.Date;

import java.util.List;
 
public class ReturnOrderRequestDTO {

    private Long purchaseOrderId;

    private String returnedByUserId;

    private Date returnDate;

    private String returnReason;

    private String status;

    private Long customerId; 


//    private String userId;

//    

//    public String getUserId() {

//		return userId;

//	}

//	public void setUserId(String userId) {

//		this.userId = userId;

//	}

    public Long getCustomerId() {

		return customerId;

	}

	public void setCustomerId(Long customerId) {

		this.customerId = customerId;

	}

	private List<ReturnOrderItemDTO> items;
 
    // Getters and Setters

    public Long getPurchaseOrderId() {

        return purchaseOrderId;

    }

    public void setPurchaseOrderId(Long purchaseOrderId) {

        this.purchaseOrderId = purchaseOrderId;

    }
 
    public String getReturnedByUserId() {

        return returnedByUserId;

    }

    public void setReturnedByUserId(String returnedByUserId) {

        this.returnedByUserId = returnedByUserId;

    }
 
    public Date getReturnDate() {

        return returnDate;

    }

    public void setReturnDate(Date returnDate) {

        this.returnDate = returnDate;

    }
 
    public String getReturnReason() {

        return returnReason;

    }

    public void setReturnReason(String returnReason) {

        this.returnReason = returnReason;

    }
 
    public String getStatus() {

        return status;

    }

    public void setStatus(String status) {

        this.status = status;

    }
 
    public List<ReturnOrderItemDTO> getItems() {

        return items;

    }

    public void setItems(List<ReturnOrderItemDTO> items) {

        this.items = items;

    }

}
 
 