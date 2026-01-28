package com.boot.ordercraft.dto;
 
public class ProductDTO {
    private Long id;
    private String name;
    private Float price;
    private Integer stock;
    private boolean scheduled;
    
    
	
	public ProductDTO(Long id, String name, Float price,Integer stock) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.stock = stock;
		this.scheduled = false;
	}
	
	 public ProductDTO(Long id, String name, Float price, Integer stock, boolean scheduled) {
	        this.id = id;
	        this.name = name;
	        this.price = price;
	        this.stock = stock;
	        this.scheduled = scheduled;
	    }
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Float getPrice() {
		return price;
	}
	public void setPrice(Float price) {
		this.price = price;
	}
	public Integer getStock() {
		return stock;
	}
	public void setStock(Integer stock) {
		this.stock = stock;
	}
	

    public boolean isScheduled() { return scheduled; }
    public void setScheduled(boolean scheduled) { this.scheduled = scheduled; }
    
 
   
  
}
 
 