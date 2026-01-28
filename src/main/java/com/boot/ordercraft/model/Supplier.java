package com.boot.ordercraft.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "SUPPLIERS")
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "supplier_seq")
    @SequenceGenerator(
        name = "supplier_seq",
        sequenceName = "SUPPLIER_SEQ",
        allocationSize = 1,
        initialValue = 1
    )
    @Column(name = "SUPPLIERSID")
    private Long suppliersId;

    @Column(name = "SUPPLIERSNAME")
    private String suppliersName;

    @Column(name = "SUPPLIERSPHONE")
    private String suppliersPhone;

    @Column(name = "SUPPLIERSEMAIL")
    private String suppliersEmail;

    @Column(name = "SUPPLIERSCONTACTPERSON")
    private String suppliersContactPerson;

    @Column(name = "SUPPLIERSADDRESS")
    private String address;

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<SupplierRating> ratings;

    // Constructors, getters, setters
    public Supplier() {}

    public Supplier(Long suppliersId, String suppliersName, String suppliersPhone, String suppliersEmail,
                    String suppliersContactPerson, String address) {
        this.suppliersId = suppliersId;
        this.suppliersName = suppliersName;
        this.suppliersPhone = suppliersPhone;
        this.suppliersEmail = suppliersEmail;
        this.suppliersContactPerson = suppliersContactPerson;
        this.address = address;
    }

    // Getters and setters...
    public String getAddress() { 
    	return address; }
    
    
    public void setAddress(String address) { this.address = address; }


	public Long getSuppliersId() {
		return suppliersId;
	}

	public void setSuppliersId(Long suppliersId) {
		this.suppliersId = suppliersId;
	}

	public String getSuppliersName() {
		return suppliersName;
	}

	public void setSuppliersName(String suppliersName) {
		this.suppliersName = suppliersName;
	}

	public String getSuppliersPhone() {
		return suppliersPhone;
	}

	public void setSuppliersPhone(String suppliersPhone) {
		this.suppliersPhone = suppliersPhone;
	}

	public String getSuppliersEmail() {
		return suppliersEmail;
	}

	public void setSuppliersEmail(String suppliersEmail) {
		this.suppliersEmail = suppliersEmail;
	}

	public String getSuppliersContactPerson() {
		return suppliersContactPerson;
	}

	public void setSuppliersContactPerson(String suppliersContactPerson) {
		this.suppliersContactPerson = suppliersContactPerson;
	}

	
    
}
