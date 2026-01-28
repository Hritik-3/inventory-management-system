package com.boot.ordercraft.model;

import jakarta.persistence.*;

@Entity
@Table(name = "CATEGORIES")
public class Category {
    @Id
    
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_seq")
    @SequenceGenerator(name = "category_seq", 
    sequenceName = "CATEGORY_SEQ", 
    allocationSize = 1, 
    initialValue = 1)
    
    @Column(name = "CATEGORIESID")
    private Long categoriesId;
    
    @Column(name = "CATEGORYNAME")
    private String categoryName;
    
    
    
	public Long getCategoriesId() {
		return categoriesId;
	}
	public void setCategoriesId(Long categoriesId) {
		this.categoriesId = categoriesId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
    
}
