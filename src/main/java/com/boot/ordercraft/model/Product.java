package com.boot.ordercraft.model;

import jakarta.persistence.*;

@Entity
@Table(name = "PRODUCTS")
public class Product {
	
	
    @Id
    
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq")
    @SequenceGenerator(
        name = "product_seq",
        sequenceName = "PRODUCT_SEQ",
        allocationSize = 1,
        initialValue = 1
    )
    @Column(name = "PRODUCTSID")
    private Long productsId;
    
    @Column(name = "PRODUCTSNAME")
    private String productsName;
    
    @Column(name = "PRODUCTSDESCRIPTION")
    private String productsDescription;
    
    @Column(name = "PRODUCTSUNITPRICE")
    private Float productsUnitPrice;
    
    @Column(name = "PRODUCTSQUANTITY")
    private Integer productsQuantity;
    
    @Column(name = "PRODUCTSIMAGE")
    private String productsImage;
    
    @Column(name = "MIN_STOCK_THRESHOLD")
    private Integer minStockThreshold;

    @Column(name = "MAX_STOCK_THRESHOLD")
    private Integer maxStockThreshold;

    public Integer getMinStockThreshold() {
		return minStockThreshold;
	}

	public void setMinStockThreshold(Integer minStockThreshold) {
		this.minStockThreshold = minStockThreshold;
	}

	public Integer getMaxStockThreshold() {
		return maxStockThreshold;
	}

	public void setMaxStockThreshold(Integer maxStockThreshold) {
		this.maxStockThreshold = maxStockThreshold;
	}

	@ManyToOne
    @JoinColumn(name = "PRODUCTSCATEGORYID", referencedColumnName = "CATEGORIESID")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "PRODUCTSSUPPLIERID", referencedColumnName = "SUPPLIERSID")
    private Supplier supplier;

	public Long getProductsId() {
		return productsId;
	}

	public void setProductsId(Long productsId) {
		this.productsId = productsId;
	}

	public String getProductsName() {
		return productsName;
	}

	public void setProductsName(String productsName) {
		this.productsName = productsName;
	}

	public String getProductsDescription() {
		return productsDescription;
	}

	public void setProductsDescription(String productsDescription) {
		this.productsDescription = productsDescription;
	}

	public Float getProductsUnitPrice() {
		return productsUnitPrice;
	}

	public void setProductsUnitPrice(Float productsUnitPrice) {
		this.productsUnitPrice = productsUnitPrice;
	}

	public Integer getProductsQuantity() {
		return productsQuantity;
	}

	public void setProductsQuantity(Integer productsQuantity) {
		this.productsQuantity = productsQuantity;
	}

	public String getProductsImage() {
		return productsImage;
	}

	public void setProductsImage(String productsImage) {
		this.productsImage = productsImage;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}
    
}
