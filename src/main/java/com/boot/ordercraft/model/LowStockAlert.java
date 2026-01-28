package com.boot.ordercraft.model;


import jakarta.persistence.*;

import java.time.Instant;
 
@Entity

@Table(name = "low_stock_alerts")
public class LowStockAlert {

		@Id

		@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "alert_seq")
		@SequenceGenerator(
		        name = "alert_seq",
		        sequenceName = "low_stock_alerts_seq",
		        allocationSize = 1,
		        initialValue = 1
		    )

		private Long id;
	 
	 
	    @Column(name = "product_id", nullable = false)

	    private Long productId;
	 
	    @Column(name = "product_name", nullable = false, length = 255)

	    private String productName;
	 
	    @Column(name = "current_stock", nullable = false)

	    private int currentStock;
	 
	    @Column(name = "threshold", nullable = false)

	    private int threshold;
	 
	    @Column(name = "status", nullable = false, length = 20)

	    private String status; // NEW, ACKNOWLEDGED, ESCALATED
	 
	    @Column(name = "created_at", nullable = false)

	    private Instant createdAt;
	 
	    public LowStockAlert() {}
	 
	    public LowStockAlert(Long id, Long productId, String productName,

	                         int currentStock, int threshold, String status, Instant createdAt) {

	        this.id = id;

	        this.productId = productId;

	        this.productName = productName;

	        this.currentStock = currentStock;

	        this.threshold = threshold;

	        this.status = status;

	        this.createdAt = createdAt;

	    }
	 
	    // getters & setters

	    public Long getId() { return id; }

	    public void setId(Long id) { this.id = id; }
	 
	    public Long getProductId() { return productId; }

	    public void setProductId(Long productId) { this.productId = productId; }
	 
	    public String getProductName() { return productName; }

	    public void setProductName(String productName) { this.productName = productName; }
	 
	    public int getCurrentStock() { return currentStock; }

	    public void setCurrentStock(int currentStock) { this.currentStock = currentStock; }
	 
	    public int getThreshold() { return threshold; }

	    public void setThreshold(int threshold) { this.threshold = threshold; }
	 
	    public String getStatus() { return status; }

	    public void setStatus(String status) { this.status = status; }
	 
	    public Instant getCreatedAt() { return createdAt; }

	    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

	}
