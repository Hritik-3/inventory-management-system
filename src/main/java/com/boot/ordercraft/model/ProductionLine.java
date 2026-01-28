package com.boot.ordercraft.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "PRODUCTION_LINE")
public class ProductionLine {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "line_seq")
	@SequenceGenerator(name = "line_seq", sequenceName = "LINE_SEQ", allocationSize = 1)
	@Column(name = "LINE_ID")
	private Long lineId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UNIT_ID", nullable = false)
    private ProductionUnit productionUnit;

    @Column(name = "LINE_NAME", nullable = false, length = 100)
    private String lineName;

    @Column(name = "CAPACITY")
    private Integer capacity;

    @Column(name = "STATUS", length = 50)
    private String status = "AVAILABLE";

    // Getters and setters
    public Long getLineId() { return lineId; }
    public void setLineId(Long lineId) { this.lineId = lineId; }

    public ProductionUnit getProductionUnit() { return productionUnit; }
    public void setProductionUnit(ProductionUnit productionUnit) { this.productionUnit = productionUnit; }

    public String getLineName() { return lineName; }
    public void setLineName(String lineName) { this.lineName = lineName; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
