package com.boot.ordercraft.model;

import jakarta.persistence.*;

@Entity
@Table(name = "RAW_MATERIALS")
public class RawMaterial {
	
	
    @Id
    
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "raw_material_seq")
    @SequenceGenerator(
        name = "raw_material_seq",
        sequenceName = "RAW_MATERIAL_SEQ",
        allocationSize = 1,
        initialValue = 1
    )
    
    @Column(name = "RWID")
    private Long rwId;
    
    @Column(name = "RWNAME")
    private String rwName;
    
    @Column(name = "RWDESCRIPTION")
    private String rwDescription;
    
    @Column(name = "RWUNITPRICE")
    private Float rwUnitPrice;
    
    @Column(name = "RWQUANTITY")
    private Integer rwQuantity;
    
    @Column(name = "RWIMAGE")
    private String rwImage;

    @ManyToOne
    @JoinColumn(name = "RWSUPPLIERID" , referencedColumnName = "SUPPLIERSID")
    private Supplier supplier;

	public Long getRwId() {
		return rwId;
	}

	public void setRwId(Long rwId) {
		this.rwId = rwId;
	}

	public String getRwName() {
		return rwName;
	}

	public void setRwName(String rwName) {
		this.rwName = rwName;
	}

	public String getRwDescription() {
		return rwDescription;
	}

	public void setRwDescription(String rwDescription) {
		this.rwDescription = rwDescription;
	}

	public Float getRwUnitPrice() {
		return rwUnitPrice;
	}

	public void setRwUnitPrice(Float rwUnitPrice) {
		this.rwUnitPrice = rwUnitPrice;
	}

	public Integer getRwQuantity() {
		return rwQuantity;
	}

	public void setRwQuantity(Integer rwQuantity) {
		this.rwQuantity = rwQuantity;
	}

	public String getRwImage() {
		return rwImage;
	}

	public void setRwImage(String rwImage) {
		this.rwImage = rwImage;
	}

	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}
    
    
}
