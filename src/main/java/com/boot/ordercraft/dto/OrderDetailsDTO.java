package com.boot.ordercraft.dto;

import java.time.LocalDate;

import java.time.LocalDateTime;

import java.util.List;

import lombok.Data;

@Data

public class OrderDetailsDTO {

    private Long poId;

    private String customerName;

    private String deliveryStatus;

    private LocalDate expectedDeliveryDate;

    private List<ItemDTO> items;


    public Long getPoId() {

		return poId;

	}


	public void setPoId(Long poId) {

		this.poId = poId;

	}


	public String getCustomerName() {

		return customerName;

	}


	public void setCustomerName(String customerName) {

		this.customerName = customerName;

	}


	public String getDeliveryStatus() {

		return deliveryStatus;

	}


	public void setDeliveryStatus(String deliveryStatus) {

		this.deliveryStatus = deliveryStatus;

	}


	public LocalDate getExpectedDeliveryDate() {

		return expectedDeliveryDate;

	}


	public void setExpectedDeliveryDate(LocalDate localDate) {

		this.expectedDeliveryDate = localDate;

	}


	public List<ItemDTO> getItems() {

		return items;

	}


	public void setItems(List<ItemDTO> items) {

		this.items = items;

	}


	@Data

    public static class ItemDTO {

        private Long itemId;

        private String productName;

        private Integer quantity;

        private Double unitPrice;

        private Integer returnedQuantity;

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

		public Integer getQuantity() {

			return quantity;

		}

		public void setQuantity(Integer quantity) {

			this.quantity = quantity;

		}

		public Double getUnitPrice() {

			return unitPrice;

		}

		public void setUnitPrice(Double unitPrice) {

			this.unitPrice = unitPrice;

		}

		public Integer getReturnedQuantity() {

			return returnedQuantity;

		}

		public void setReturnedQuantity(Integer returnedQuantity) {

			this.returnedQuantity = returnedQuantity;

		}


    }

}

 