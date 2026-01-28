package com.boot.ordercraft.dto;
 
import java.time.LocalDate;

import java.time.LocalDateTime;

import java.util.List;
 
public class ViewOrderDTO {

    private Long poId;

    private LocalDateTime orderDate;

    private LocalDate expectedDeliveryDate;

    private String deliveryStatus;

    private Double totalAmount;

    private String customerName;

    private List<ItemInfo> items;
 
    public static class ItemInfo {

        private Long itemId;

        private Long productId;

		private String productName;

        private int quantity;

        private int returnedQuantity;

        public Long getProductId() {

    			return productId;

    		}

        public void setProductId(Long productId) {

    			this.productId = productId;

    		}

		public Long getItemId() {

			return itemId;

		}

		public void setItemId(Long itemId) {

			this.itemId = itemId;

		}

		public String getProductName() {

			return productName;

		}

		public void setProductName(String productName) {

			this.productName = productName;

		}

		public int getQuantity() {

			return quantity;

		}

		public void setQuantity(int quantity) {

			this.quantity = quantity;

		}

		public int getReturnedQuantity() {

			return returnedQuantity;

		}

		public void setReturnedQuantity(int returnedQuantity) {

			this.returnedQuantity = returnedQuantity;

		}


    }
 
	public Long getPoId() {

		return poId;

	}
 
	public void setPoId(Long poId) {

		this.poId = poId;

	}
 
	public LocalDateTime getOrderDate() {

		return orderDate;

	}
 
	public void setOrderDate(LocalDateTime orderDate) {

		this.orderDate = orderDate;

	}
 
	public LocalDate getExpectedDeliveryDate() {

		return expectedDeliveryDate;

	}
 
	public void setExpectedDeliveryDate(LocalDate expectedDeliveryDate) {

		this.expectedDeliveryDate = expectedDeliveryDate;

	}
 
	public String getDeliveryStatus() {

		return deliveryStatus;

	}
 
	public void setDeliveryStatus(String deliveryStatus) {

		this.deliveryStatus = deliveryStatus;

	}
 
	public Double getTotalAmount() {

		return totalAmount;

	}
 
	public void setTotalAmount(Double totalAmount) {

		this.totalAmount = totalAmount;

	}
 
	public String getCustomerName() {

		return customerName;

	}
 
	public void setCustomerName(String customerName) {

		this.customerName = customerName;

	}
 
	public List<ItemInfo> getItems() {

		return items;

	}
 
	public void setItems(List<ItemInfo> items) {

		this.items = items;

	}


}

 